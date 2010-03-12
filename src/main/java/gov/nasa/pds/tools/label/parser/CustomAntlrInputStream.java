package gov.nasa.pds.tools.label.parser;

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;

// NOTE: if mark and reset is used for anything other than resetting to start of file or just after SFDU headers, the logic will need to be updated to account for the fact that you may be resetting into an unknown state. A possible solution could be to store a last read location for history and short circuit the test until that position has been reached again. 
public class CustomAntlrInputStream extends FilterInputStream {

    enum State {
        NORMAL, // in the label
        IN_SINGLE_QUOTES, // in single quotes, ignore END until closed
        IN_DOUBLE_QUOTES, // in double quotes, ignore END until closed
        IN_COMMENTS, // in comments, ignore END until closed
        FOUND_END, // found END, only whitespace valid now
    }

    // buffer used to determine if you have reached END
    private byte[] history = { ' ', ' ', ' ', ' ', ' ' };

    // current position in stream
    private int pos = 0;

    // marked position
    // private int markPos = 0;

    // last read location for history, used so that mark() and reset() do not
    // break state testing
    // private int historyPos = 0;

    // location of either EOF or first non-whitespace char after END statement
    private int endOfLabel = -1;

    private Integer attachedContentStartByte = null;

    private boolean hasBlankFill = false;

    // current state of the read
    private State state = State.NORMAL;

    protected volatile BufferedInputStream in;

    public CustomAntlrInputStream(BufferedInputStream in) {
        super(in);
        this.in = in;
    }

    public Integer getAttachedContentStartByte() {
        return this.attachedContentStartByte;
    }

    public boolean hasBlankFill() {
        return this.hasBlankFill;
    }

    @Override
    public synchronized int read(byte b[], int off, int len) throws IOException {
        {
            if (off < 0 || len < 0 || len > (b.length - off)) {
                throw new IndexOutOfBoundsException();
            } else if (len == 0) {
                return 0;
            }

            int count = 0;
            while (count < len) {
                int r = read();
                if (r < 0) {
                    break;
                }
                b[count + off] = (byte) r;
                count++;
            }
            if (count == 0) {
                return -1;
            }
            return count;
        }
    }

    @Override
    public int read() throws IOException {

        int b = this.in.read();
        ++this.pos;

        // use end of label location rather than state to indicate reaching the
        // end so that mark and reset do not break things
        if (this.endOfLabel != -1 && this.pos >= this.endOfLabel) {
            return -1;
        }

        // skip special handling if reset to previously read byte
        // if (this.pos < this.historyPos) {
        // return b;
        // }

        // tests are ordered by likelihood of state occurring (order guess by
        // jagander)
        if (this.state == State.IN_SINGLE_QUOTES && b == '\'') {
            this.state = State.NORMAL;
        } else if (this.state == State.IN_DOUBLE_QUOTES && b == '"') {
            this.state = State.NORMAL;
        } else if (this.state == State.IN_COMMENTS && b == '/'
                && this.history[0] == '*') {
            this.state = State.NORMAL;
        } else if (this.state == State.NORMAL && b == '\'') {
            this.state = State.IN_SINGLE_QUOTES;
        } else if (this.state == State.NORMAL && b == '"') {
            this.state = State.IN_DOUBLE_QUOTES;
        } else if (this.state == State.NORMAL && b == '*'
                && this.history[0] == '/') {
            this.state = State.IN_COMMENTS;
        } else if (this.state == State.NORMAL && b != -1) {
            if (Character.isWhitespace(b)) {
                // Check for END just prior.
                if (Character.isWhitespace(this.history[3])
                        && this.history[2] == 'E' && this.history[1] == 'N'
                        && this.history[0] == 'D') {
                    this.state = State.FOUND_END;
                }
            }
        } else if (this.state == State.FOUND_END) {
            if (!Character.isWhitespace(b)) {
                this.endOfLabel = this.pos;
                // if end of label is not EOF, this is attached data start byte
                if (b != -1) {
                    this.attachedContentStartByte = this.pos;
                }
                // 32 == ' '
            } else if (b == 32) {
                this.hasBlankFill = true;
            }
        } else if (b == -1) {
            // missing END back up one space
            this.endOfLabel = this.pos - 1;

        }

        // this.historyPos++;
        this.history[3] = this.history[2];
        this.history[2] = this.history[1];
        this.history[1] = this.history[0];
        this.history[0] = (byte) b;

        return b;
    }

}
