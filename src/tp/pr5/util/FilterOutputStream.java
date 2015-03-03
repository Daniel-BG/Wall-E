package tp.pr5.util;

import java.io.IOException;
import java.io.OutputStream;

public class FilterOutputStream extends OutputStream {
    private OutputStream out;
    private boolean isWriting;
    public FilterOutputStream(OutputStream out, boolean isWriting) {
        this.out = out;
        this.isWriting = isWriting;
    }


    @Override
    public void write(int b) throws IOException {
        if (isWriting)
        	out.write(b);
    }

}