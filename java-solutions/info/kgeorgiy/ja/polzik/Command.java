package info.kgeorgiy.ja.polzik;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public enum Command {

    LS(Set.of("dir", "ls"), 0) {
        @Override
        public Path execute(final CommandInfo info) {
            try {

                System.out.format("%s : %n`%s`%n",
                        info.resourceBundle.getString("Current dir"),
                        info.currentDirectory.toString());
                Arrays.stream(
                        Objects.requireNonNull(info.currentDirectory.toFile().list())
                ).forEach(System.out::println);
            } catch (NullPointerException e) {
                System.err.format("%s : `%s`%n",
                        info.resourceBundle.getString("Err"),
                        e.getLocalizedMessage());
            }
            return info.currentDirectory;
        }
    },

    RM(Set.of("del", "rm"), 1) {
        @Override
        public Path execute(final CommandInfo info) {
            final Path path = Paths.get(info.args[0]);
            try {
                Files.delete(info.currentDirectory.resolve(path));
                System.out.printf("%s `%s` %s%n",
                        info.resourceBundle.getString("File"),
                        path,
                        info.resourceBundle.getString("deleted"));
            } catch (NoSuchFileException e) {
                System.err.format("%s : `%s`%n",
                        info.resourceBundle.getString("No file"),
                        e.getLocalizedMessage());
            } catch (DirectoryNotEmptyException e) {
                System.err.format("%s : `%s`%n",
                        info.resourceBundle.getString("File is directory"),
                        e.getLocalizedMessage());
            } catch (IOException e) {
                System.err.format("%s : `%s`%n",
                        info.resourceBundle.getString("I/O"),
                        e.getLocalizedMessage());
            }
            return info.currentDirectory;
        }
    },

    CD(Set.of("cd"), 1) {
        @Override
        public Path execute(final CommandInfo info) {
            final Path path = Paths.get(info.args[0]);
            if (Files.exists(info.currentDirectory.resolve(path))) {
                if (Files.isDirectory(info.currentDirectory.resolve(path))) {
                    Path currentDirectory = info.currentDirectory.resolve(path).normalize();
                    System.out.format("%s : `%s`%n",
                            info.resourceBundle.getString("Move to new directory"),
                            path);
                    return currentDirectory;
                } else {
                    System.err.format("`%s` - %s%n",
                            info.resourceBundle.getString("isn't directory"),
                            path);
                }
            } else {
                System.err.format("`%s` - %s%n",
                        path,
                        info.resourceBundle.getString("doesn't exist"));
            }
            return info.currentDirectory;
        }
    },

    CAT(Set.of("cat", "type"), 1) {
        @Override
        public Path execute(final CommandInfo info) {
            final Path path = Paths.get(info.args[0]);
            try {
                BufferedReader br = new BufferedReader(new FileReader(path.toFile()));
                System.out.format("%s:%n",
                        info.resourceBundle.getString("File contents"));
                while (br.ready()) {
                    System.out.println(br.readLine());
                }
            } catch (FileNotFoundException e) {
                System.err.format("%s : `%s`%n",
                        info.resourceBundle.getString("No file"),
                        e.getLocalizedMessage());
            } catch (IOException e) {
                System.err.format("%s : `%s`%n",
                        info.resourceBundle.getString("I/O"),
                        e.getLocalizedMessage());
            }
            return info.currentDirectory;
        }
    },

    CREATE(Set.of("create"), 1) {
        @Override
        public Path execute(final CommandInfo info) {
            final Path path = Paths.get(info.args[0]);
            try {
                Files.createFile(info.currentDirectory.resolve(path));
                System.out.printf("%s `%s` %s%n",
                        info.resourceBundle.getString("File"),
                        path,
                        info.resourceBundle.getString("created"));
            } catch (FileAlreadyExistsException e) {
                System.err.format("%s : %s%n",
                        info.resourceBundle.getString("File/dir is exists"),
                        e.getLocalizedMessage());
            } catch (IOException e) {
                System.err.format("%s : `%s`%n",
                        info.resourceBundle.getString("I/O"),
                        e.getLocalizedMessage());
            }
            return info.currentDirectory;
        }
    },

    MD_DIR(Set.of("mkdir"), 1) {
        @Override
        public Path execute(final CommandInfo info) {
            final Path path = Paths.get(info.args[0]);
            try {
                Files.createDirectories(info.currentDirectory.resolve(path));
                System.out.printf("%s `%s` %s%n",
                        info.resourceBundle.getString("Directory"),
                        path,
                        info.resourceBundle.getString("created (she)"));
            } catch (FileAlreadyExistsException e) {
                System.err.format("%s : %s%n",
                        info.resourceBundle.getString("File/dir is exists"),
                        e.getLocalizedMessage());
            } catch (IOException e) {
                System.err.format("%s : `%s`%n",
                        info.resourceBundle.getString("I/O"),
                        e.getLocalizedMessage());
            }
            return info.currentDirectory;
        }
    },

    RM_DIR(Set.of("rmdir"), 1) {
        @Override
        public Path execute(final CommandInfo info) {
            final Path path = Paths.get(info.args[0]);
            try {
                Files.walkFileTree(info.currentDirectory.resolve(path), new DeleteFileWalker());
                System.out.printf("%s `%s` %s%n",
                        info.resourceBundle.getString("Directory"),
                        path,
                        info.resourceBundle.getString("deleted (she)"));
            } catch (IOException e) {
                System.err.format("%s : `%s`%n",
                        info.resourceBundle.getString("I/O"),
                        e.getLocalizedMessage());
            }
            return info.currentDirectory;
        }
    };

    private final Set<String> tokens;
    private final int argsSize;

    public abstract Path execute(final CommandInfo commandInfo);

    public int getArgsSize() {
        return argsSize;
    }

    public boolean validate(final CommandInfo commandInfo) {
        return (commandInfo.args.length == argsSize);
    }

    Command(final Set<String> tokens, final int argsSize) {
        this.tokens = tokens;
        this.argsSize = argsSize;
    }

    private static final Map<String, Command> tokenToCommand = Arrays.stream(Command.values())
            .flatMap(command ->
                    command.tokens.stream()
                            .map(token -> Map.entry(token, command))
            ).collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue
            ));

    public static Command getCommand(final String str) {
        return tokenToCommand.get(str);
    }

    public static class CommandInfo {
        final ResourceBundle resourceBundle;
        final Path currentDirectory;
        final String[] args;

        public CommandInfo(final Path currentDirectory,
                           final ResourceBundle resourceBundle,
                           final String[] args) {
            this.currentDirectory = currentDirectory;
            this.resourceBundle = resourceBundle;
            this.args = args;
        }
    }
}
