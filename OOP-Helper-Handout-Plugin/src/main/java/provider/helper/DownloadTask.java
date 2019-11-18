package provider.helper;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ntp.TimeStamp;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
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

    private DownloadTask() { }


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
            }
        }
    }

    //get commit messages ahead of local repository
    public ArrayList<String> getLatestCommits() {
        System.out.println("checkIfNewVersionIsAvailable");
        //https://github.com/centic9/jgit-cookbook/blob/master/src/main/java/org/dstadler/jgit/porcelain/ShowLog.java
        ArrayList<String> commitMessages = new ArrayList<>();
        Git git = null;
        try {
            git = Git.open(new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE + "/.git"));
            Repository repository = git.getRepository();
            Ref head = repository.getAllRefs().get("HEAD");
            String lastLocalCommitId = head.getObjectId().getName();
            System.out.println("Ref of HEAD: " + lastLocalCommitId);
            git.fetch().call();
            System.out.println("git.fetch Branch: " + git.getRepository().getBranch());
            Iterable<RevCommit> logs = git.log().call();
            logs = git.log()
                    .add(repository.resolve("remotes/origin/test"))
                    .call();
            for (RevCommit rev : logs) {
                String currentRevID = rev.getId().getName();
                if (currentRevID.equals(lastLocalCommitId)) {
                    break;
                }
                System.out.println("Commit  Text Add: " + rev.getFullMessage());
                commitMessages.add(rev.getFullMessage());
            }
            repository.close();
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
            System.out.println(e);
        }
        return commitMessages;
    }

    public void updateRepository(String repoUrl) throws IOException, GitAPIException {
        Git git;
        git = Git.open(new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE));
        git.pull().call();
    }
}
