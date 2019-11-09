package provider.helper;

import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ntp.TimeStamp;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.DepthWalk;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepository;
import provider.RepoLocalStorageDataProvider;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static environment.Constants.*;

//is singleton
public class DownloadTask {

    private String path;
    private static DownloadTask single_instance = null;
    private static Git clone = null;

   /* public DownloadTask(String path) {
        this.path = path;
    }*/


    public static DownloadTask getInstance() {
        if (single_instance == null) {
            single_instance = new DownloadTask();
        }
        return single_instance;
    }

    private DownloadTask() {
    }


    public void run(String repoUrl, File contentRepoFile, String branchPath) throws IOException {
        clone = null;
        {
            try {
                clone = Git.cloneRepository()
                        .setURI(repoUrl)
                        .setDirectory(contentRepoFile)
                        .setBranchesToClone(Arrays.asList(branchPath))
                        .setBranch(branchPath)
                        .call();
            } catch (GitAPIException e) {
                e.printStackTrace();
            } finally {
                System.out.println("clone run");
                Ref head = clone.getRepository().getAllRefs().get("HEAD");
                System.out.println("Ref of HEAD: " + head + ": " + head.getName() + " - " + head.getObjectId().getName());
                String lastCommitHash = head.getObjectId().getName();
                saveLastCommitHash(lastCommitHash);
            }
        }

    /*System.out.println("Start downloading [" + this.path + "] to " + output.getAbsolutePath());
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
        }*/


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


    private void saveLastCommitHash(String lastCommitHash) throws IOException {
        System.out.println("saveLastCommitHash run");
        File lastCommitHashFile = new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + LAST_COMMIT_HASH_FILE);
        lastCommitHashFile.createNewFile();
        //https://stackoverflow.com/a/1053475
        //PrintWriter out = new PrintWriter(lastCommitHashFile);
        //out.println(lastCommitHash);

        //https://stackoverflow.com/a/1053487
        //FileUtils.writeStringToFile(new File("test.txt"), "Hello File");

        try (PrintWriter out = new PrintWriter(lastCommitHashFile)) {
            out.println(lastCommitHash);
        }
    }

    //https://stackoverflow.com/a/13944180
    public boolean checkIfNewVersionIsAvailable() {
        System.out.println("checkIfNewVersionIsAvailable");
        //TODO compare Last Commits
        try {
            Git git = Git.open(new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE + "/.git"));
            //Git git = new Git(repository);
            Repository repository = git.getRepository();

            git.fetch().call();
            System.out.println("git.fetch Branch: " + git.getRepository().getBranch());

            Iterable<RevCommit> logs = git.log().call();
            int count = 0;
            logs = git.log()
                    .add(repository.resolve("remotes/origin/test"))
                    .call();
            count = 0;
            for (RevCommit rev : logs) {
                System.out.println("Commit: " + rev /* + ", name: " + rev.getName() + ", id: " + rev.getId().getName() */);
                count++;
            }
            System.out.println("Had " + count + " commits overall on test-branch");



        } catch (IOException | GitAPIException  e ) {
            e.printStackTrace();
            System.out.println(e);
        }
        return false;
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
    public boolean compareZipFiles(File file1, File file2) throws IOException {
        System.out.println("TimeBefore: " + TimeStamp.getCurrentTime().toDateString());

        InputStream theFile1 = new FileInputStream(file1);
        ZipInputStream stream = new ZipInputStream(theFile1);

        InputStream theFile2 = new FileInputStream(file2);
        ZipInputStream stream2 = new ZipInputStream(theFile2);

        try {
            ZipEntry entry;
            while ((entry = stream.getNextEntry()) != null) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                DigestInputStream dis = new DigestInputStream(stream, md);
                DigestInputStream dis2 = new DigestInputStream(stream2, md);

                byte[] buffer = new byte[1024];
                int read = dis.read(buffer);
                int read2 = dis2.read(buffer);
                while (read > -1) {
                    read = dis.read(buffer);
                    read2 = dis.read(buffer);
                }
                System.out.println(entry.getName() + ": "
                        + Arrays.toString(dis.getMessageDigest().digest()));
                System.out.println(entry.getName() + ": "
                        + Arrays.toString(dis2.getMessageDigest().digest()));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            stream.close();
        }


        try {
            return FileUtils.contentEquals(file1, file2);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Time After: " + TimeStamp.getCurrentTime().toDateString());
        }
        return false;

    }

    public ArrayList<String> getLastCommitMassages() {
        return null;
    }
}
