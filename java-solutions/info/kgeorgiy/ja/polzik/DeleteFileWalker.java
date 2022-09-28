package info.kgeorgiy.ja.polzik;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class DeleteFileWalker extends SimpleFileVisitor<Path> {

    @Override
    public FileVisitResult visitFile(final Path file,
                                     final BasicFileAttributes attrs) throws IOException {
        Files.delete(file);
        return super.visitFile(file, attrs);
    }

    @Override
    public FileVisitResult postVisitDirectory(final Path dir,
                                              final IOException exc) throws IOException {
        Files.delete(dir);
        return super.postVisitDirectory(dir, exc);
    }
}
