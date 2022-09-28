package info.kgeorgiy.ja.polzik;

import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main {

    private Path currentDir;
    private final ResourceBundle usageResourceBundle;

    public Main(final Path currentDir,
                final ResourceBundle usageResourceBundle) {
        this.currentDir = currentDir;
        this.usageResourceBundle = usageResourceBundle;
    }

    public static void main(final String[] args) {
        if (args.length > 1) {
            System.err.println("Wrong arguments format. Expected: `Main [locale]`");
        }
        final Locale locale = (args.length == 1) ? new Locale(args[0]) : Locale.getDefault();
        Locale.setDefault(locale);
        final ResourceBundle resourceBundle = ResourceBundle.getBundle(
                "info.kgeorgiy.ja.polzik.bundle.UsageResourceBundle",
                locale
        );
        final Path currentDir = Paths.get(System.getProperty("user.dir"));
        try {
            new Main(currentDir, resourceBundle).run();
        } catch (Exception e) {
            System.err.println("Unexpected exception occurred: " + e.getLocalizedMessage());
        }
    }

    private void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String[] args = line.split(" ");
                final Command command = Command.getCommand(args[0]);
                if (command == null) {
                    System.err.println(
                            usageResourceBundle.getString("command not exist"));
                    continue;
                }
                final Command.CommandInfo info = new Command.CommandInfo(
                        currentDir,
                        usageResourceBundle,
                        Arrays.copyOfRange(args, 1, args.length)
                );
                if (command.validate(info)) {
                    try {
                        currentDir = command.execute(info);
                    } catch (Exception e) {
                        System.err.format("%s: %s%n",
                                usageResourceBundle.getString("Unexpected exception occurred"),
                                e.getLocalizedMessage());
                    }
                } else {
                    System.err.format("%s: %d, %s: %d%n",
                            "Expected",
                            command.getArgsSize(),
                            "Actual",
                            info.args.length);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
