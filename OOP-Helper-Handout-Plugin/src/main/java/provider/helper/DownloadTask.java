package provider.helper;

import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ntp.TimeStamp;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import provider.LocalStorageDataProvider;
import provider.RepoLocalStorageDataProvider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class DownloadTask {

    private String path;

    public DownloadTask(String path) {
        this.path = path;
    }

    public void run(File output) {
        System.out.println("Start downloading [" + this.path + "] to " + output.getAbsolutePath());
        try (BufferedInputStream in = new BufferedInputStream(new URL(this.path).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(output)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            System.out.println("Finished download file");
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*FileRepositoryBuilder builder = new FileRepositoryBuilder();
        File file = new File(RepoLocalStorageDataProvider.getRepoLocalFile();
        try (Repository repository = builder.setGitDir(file)
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build()) {
            System.out.println("Having repository: " + repository.getDirectory());

            // the Ref holds an ObjectId for any type of object (tree, commit, blob, tree)
            Ref head = repository.exactRef("refs/heads/master");
            System.out.println("Ref of refs/heads/master: " + head);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Repository repository = null;
        //Repository contentDataRepo = Repository.
        Ref head = repository.getAllRefs().get("HEAD");
        System.out.println("Ref of HEAD: " + head + ": " + head.getName() + " - " + head.getObjectId().getName());*/
     /*   File currentFile = new File(".");
        try {
            Git git = Git.init().setDirectory(currentFile).call();
            Repository repository = git.getRepository();
            ObjectId head = repository.resolve(Constants.HEAD);
            Iterable<RevCommit> commits = git.log()
                    .add(head)
                    .addPath(RepoLocalStorageDataProvider.getRepoLocalFile())
                    .setMaxCount(1).call();
            commits.forEach(revCommit -> System.out.println("commit: " + revCommit.getName())); // getName will print the revision hash
        } catch (IOException | GitAPIException e) {
            System.out.println("commit crash");

            e.printStackTrace();
        }*/



    }

    //https://stackoverflow.com/a/14656534
    public void unzipFile(File in, File out) {
        try {
            ZipFile zipFile = new ZipFile(in);
            zipFile.extractAll(out.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //https://stackoverflow.com/a/27379126
    public boolean compareZipFiles(File file1, File file2) {
        System.out.println("TimeBefore: " + TimeStamp.getCurrentTime().toDateString());
        //Deprecated
       /* try {
            return Files.hash(file1, Hashing.md5()).equals(Files.hash(file2, Hashing.sha1()));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        try {
            return FileUtils.contentEquals(file1, file2);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("Time After: " +TimeStamp.getCurrentTime().toDateString());
        }
        return false;
    }

}
