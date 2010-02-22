package gov.nasa.pds.tools.label.parser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class CustomAntlrInputStream extends BufferedInputStream {

    private int endOfLabel = -1;

    enum State {
        NORMAL, IN_SINGLE_QUOTES, IN_DOUBLE_QUOTES, IN_COMMENTS
    }

    private static final AtomicReferenceFieldUpdater<BufferedInputStream, byte[]> bufUpdater = AtomicReferenceFieldUpdater
            .newUpdater(BufferedInputStream.class, byte[].class, "buf"); //$NON-NLS-1$

    public CustomAntlrInputStream(InputStream in) {
        super(in);
        // set end location
        initEndStatement();
    }

    public CustomAntlrInputStream(InputStream in, int size) {
        super(in, size);
        // set end location
        initEndStatement();
    }

    private void initEndStatement() {
        State state = State.NORMAL;
        // ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte history[] = { ' ', ' ', ' ', ' ', ' ' };
        boolean eofNotFound = true;

        try {
            int lastPos = -1;
            while (eofNotFound) {
                lastPos = this.pos;
                int b = readOrig();

                // buffer.write(b);
                if (state == State.IN_SINGLE_QUOTES && b == '\'') {
                    state = State.NORMAL;
                } else if (state == State.IN_DOUBLE_QUOTES && b == '"') {
                    state = State.NORMAL;
                } else if (state == State.IN_COMMENTS && b == '/'
                        && history[3] == '*') {
                    state = State.NORMAL;
                } else if (state == State.NORMAL && b == '\'') {
                    state = State.IN_SINGLE_QUOTES;
                } else if (state == State.NORMAL && b == '"') {
                    state = State.IN_DOUBLE_QUOTES;
                } else if (state == State.NORMAL && b == '*'
                        && history[3] == '/') {
                    state = State.IN_COMMENTS;
                } else if (state == State.NORMAL) {
                    if (b == '\r' || b == '\n' || b == ' ' || b == -1) {
                        // Check for END just prior.
                        if (history[1] == 'E' && history[2] == 'N'
                                && history[3] == 'D'
                                && Character.isWhitespace(history[0])) {
                            eofNotFound = false;
                            // only keep reading past end if there is something
                            // past end
                            if (this.pos != this.count) {
                                int afterEnd = readOrig();
                                while (Character.isWhitespace(afterEnd)) {
                                    afterEnd = readOrig();
                                }
                            }
                            // use last position if read to eof and pos zeroes
                            // out
                            this.endOfLabel = this.pos == 0 ? lastPos
                                    : this.pos;
                            break;
                        }
                    }

                    history[0] = history[1];
                    history[1] = history[2];
                    history[2] = history[3];
                    history[3] = (byte) b;
                } else if (b == -1) {
                    // missing END
                    this.endOfLabel = lastPos;
                    break;
                }

            }
            // reset pos now that you've got your ending position
            getBufIfOpen();
            this.pos = 0;
        } catch (final IOException ioe) {
            ioe.printStackTrace();
            // TODO: determine what to do with the exception
        }

    }

    @Override
    public int read() throws IOException {
        if (foundLastLabelByte()) {
            return -1;
        }
        return readOrig();
    }

    public synchronized int readOrig() throws IOException {
        if (this.pos >= this.count) {
            fill();
            if (this.pos >= this.count)
                return -1;
        }
        return getBufIfOpen()[this.pos++] & 0xff;
    }

    private byte[] getBufIfOpen() throws IOException {
        byte[] buffer = this.buf;
        if (buffer == null)
            throw new IOException("Stream closed"); //$NON-NLS-1$
        return buffer;
    }

    private InputStream getInIfOpen() throws IOException {
        InputStream input = this.in;
        if (input == null)
            throw new IOException("Stream closed"); //$NON-NLS-1$
        return input;
    }

    @Override
    public synchronized int read(byte b[], int off, int len) throws IOException {
        {
            getBufIfOpen(); // Check for closed stream
            if ((off | len | (off + len) | (b.length - (off + len))) < 0) {
                throw new IndexOutOfBoundsException();
            } else if (len == 0) {
                return 0;
            }

            int n = 0;
            for (;;) {
                int nread = read1(b, off + n, len - n);
                if (nread <= 0)
                    return (n == 0) ? nread : n;
                n += nread;
                if (n >= len)
                    return n;
                // if not closed but no bytes available, return
                InputStream input = this.in;
                if (input != null && input.available() <= 0)
                    return n;
            }
        }
    }

    private int read1(byte[] b, int off, int len) throws IOException {
        if (foundLastLabelByte()) {
            return -1;
        }
        int avail = -1;
        if (this.endOfLabel != -1) {
            avail = this.endOfLabel - this.pos;
        } else {
            avail = this.count - this.pos;
        }
        if (avail <= 0) {
            /*
             * If the requested length is at least as large as the buffer, and
             * if there is no mark/reset activity, do not bother to copy the
             * bytes into the local buffer. In this way buffered streams will
             * cascade harmlessly.
             */
            if (len >= getBufIfOpen().length && this.markpos < 0) {

                return getInIfOpen().read(b, off, getReadLength(len));
            }
            fill();
            avail = this.count - this.pos;
            if (avail <= 0)
                return -1;
        }
        int cnt = (avail < len) ? avail : len;
        System.arraycopy(getBufIfOpen(), this.pos, b, off, cnt);
        this.pos += cnt;
        return cnt;
    }

    private void fill() throws IOException {
        byte[] buffer = getBufIfOpen();
        if (this.markpos < 0)
            this.pos = 0; /* no mark: throw away the buffer */
        else if (this.pos >= buffer.length) /* no room left in buffer */
            if (this.markpos > 0) { /* can throw away early part of the buffer */
                int sz = this.pos - this.markpos;
                System.arraycopy(buffer, this.markpos, buffer, 0, sz);
                this.pos = sz;
                this.markpos = 0;
            } else if (buffer.length >= this.marklimit) {
                this.markpos = -1; /* buffer got too big, invalidate mark */
                this.pos = 0; /* drop buffer contents */
            } else { /* grow buffer */
                int nsz = this.pos * 2;
                if (nsz > this.marklimit)
                    nsz = this.marklimit;
                byte nbuf[] = new byte[nsz];
                System.arraycopy(buffer, 0, nbuf, 0, this.pos);
                if (!bufUpdater.compareAndSet(this, buffer, nbuf)) {
                    // Can't replace buf if there was an async close.
                    // Note: This would need to be changed if fill()
                    // is ever made accessible to multiple threads.
                    // But for now, the only way CAS can fail is via close.
                    // assert buf == null;
                    throw new IOException("Stream closed"); //$NON-NLS-1$
                }
                buffer = nbuf;
            }
        this.count = this.pos;
        int length = getReadLength(buffer.length - this.pos);
        int n = getInIfOpen().read(buffer, this.pos, length);
        if (n > 0)
            this.count = n + this.pos;
    }

    public int readSimple(byte b[], int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0)
                || ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        int c = readOrig();
        if (c == -1) {
            return -1;
        }
        b[off] = (byte) c;

        int i = 1;
        try {
            for (; i < len; i++) {
                c = readOrig();
                if (c == -1) {
                    break;
                }
                if (b != null) {
                    b[off + i] = (byte) c;
                }
            }
        } catch (IOException ee) {
            // noop
        }
        return i;
    }

    private boolean foundLastLabelByte() {
        if (this.pos >= this.endOfLabel && this.endOfLabel != -1) {
            return true;
        }
        return false;
    }

    // determine how much data to read so that you don't go past end
    private int getReadLength(int desired) {
        if (this.endOfLabel == -1) {
            return desired;
        }
        return (this.pos + desired) > this.endOfLabel ? this.endOfLabel
                - this.pos : desired;
    }

}
