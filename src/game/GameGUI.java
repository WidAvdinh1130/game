package game;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.awt.event.ActionEvent;

public class GameGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private final JTextArea output = new JTextArea();
    private final JTextArea input = new JTextArea();
    private final BlockingQueue<String> lines = new LinkedBlockingQueue<>();
    private boolean clearOnNextPrint = false;

    @SuppressWarnings("serial")
    public GameGUI() {
        super("Text Adventure");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);

        Color black = new Color(0, 0, 0);
        Color outputColor = new Color(0, 252, 248);
        Color inputColor = new Color(21, 252, 0);
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 14);

        // Output box (read-only)
        output.setEditable(false);
        output.setLineWrap(true);
        output.setWrapStyleWord(true);
        output.setFont(font);
        output.setMargin(new Insets(6, 6, 6, 6));
        output.setBackground(black);
        output.setForeground(outputColor);

        // Input box (Enter submits)
        input.setLineWrap(true);
        input.setWrapStyleWord(true);
        input.setFont(font);
        input.setMargin(new Insets(6, 6, 6, 6));
        input.setBackground(black);
        input.setForeground(inputColor);
        input.setCaretColor(inputColor);   // keeps the cursor visible on black
        input.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "submit");
        input.getActionMap().put("submit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submit();
            }
        });
        Color red = new Color(255, 0, 0);

        JScrollPane outputScroll = new JScrollPane(output);
        JScrollPane inputScroll = new JScrollPane(input);
        for (JScrollPane sp : new JScrollPane[] { outputScroll, inputScroll }) {
            sp.getViewport().setBackground(black);
            sp.setBorder(BorderFactory.createLineBorder(red, 2));
        }

        // Two equal columns, side by side
        JPanel panel = new JPanel(new GridLayout(1, 2, 2, 0));
        panel.setBackground(black);
        panel.add(outputScroll);
        panel.add(inputScroll);

        setContentPane(panel);
        setVisible(true);
        input.requestFocusInWindow();
    }

    private void submit() {
        String text = input.getText();
        input.setText("");
        clearOnNextPrint = true;   // next output replaces the old one
        lines.add(text);
    }
    /** Append text to the output box (safe to call from any thread). */
    public void print(String text) {
        SwingUtilities.invokeLater(() -> {
            if (clearOnNextPrint) {
                output.setText("");
                clearOnNextPrint = false;
            }
            output.append(text);
            output.setCaretPosition(output.getDocument().getLength());
        });
    }

    public void println(String text) {
        print(text + "\n");
    }

    /** Blocks until the player presses Enter, then returns the line typed. */
    public String readLine() throws InterruptedException {
        return lines.take();
    }

    /**
     * Redirects System.out to the output box and System.in to the input box,
     * so existing console code (System.out.println, Scanner(System.in)) works
     * unchanged. Call this BEFORE creating your Scanner.
     */
    public void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            @Override
            public synchronized void write(int b) {
                buffer.write(b);
            }

            @Override
            public synchronized void write(byte[] b, int off, int len) {
                buffer.write(b, off, len);
            }

            @Override
            public synchronized void flush() {
                if (buffer.size() > 0) {
                    print(buffer.toString(StandardCharsets.UTF_8));
                    buffer.reset();
                }
            }
        };
        System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
        System.setErr(new PrintStream(out, true, StandardCharsets.UTF_8));
        System.setIn(new GuiInputStream());
    }

    /** Feeds lines typed in the GUI to anything reading System.in. */
    private class GuiInputStream extends InputStream {
        private byte[] buf = new byte[0];
        private int pos = 0;

        private void fill() throws IOException {
            while (pos >= buf.length) {
                try {
                    buf = (lines.take() + "\n").getBytes(StandardCharsets.UTF_8);
                    pos = 0;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new InterruptedIOException();
                }
            }
        }

        @Override
        public int read() throws IOException {
            fill();
            return buf[pos++] & 0xFF;
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (len == 0) return 0;
            fill();
            int n = Math.min(len, buf.length - pos);
            System.arraycopy(buf, pos, b, off, n);
            pos += n;
            return n;
        }
    }
    public static void main(String[] args) throws Exception {
        // Build the window on the Swing thread
        final GameGUI[] holder = new GameGUI[1];
        SwingUtilities.invokeAndWait(() -> holder[0] = new GameGUI());
        GameGUI gui = holder[0];

        // Route System.out to the output box and System.in to the input box
        gui.redirectSystemStreams();

        // Run the game on its own thread so the window never freezes
        Thread gameThread = new Thread(() -> Game.main(args), "game-thread");
        gameThread.setDaemon(true);   // thread ends when the window is closed
        gameThread.start();
    }
}