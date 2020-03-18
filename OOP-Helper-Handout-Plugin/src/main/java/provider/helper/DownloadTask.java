package provider.helper;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import provider.RepoLocalStorageDataProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static environment.FileConstants.LOCAL_STORAGE_FILE;
import static environment.FileConstants.REPO_LOCAL_STORAGE_FILE;

//is singleton
public class DownloadTask {
    private String path;
    private static DownloadTask single_instance = null;
    private static Git clone;

    public static DownloadTask getInstance() {
        if (single_instance == null) {
            single_instance = new DownloadTask();
        }
        return single_instance;
    }

    private DownloadTask() { }

    //https://www.vogella.com/tutorials/JGit/article.html
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
                System.out.println("run failed");
                e.printStackTrace();
            } finally {
                System.out.println("clone run");
            }
        }
    }

    //get commit messages ahead of local repository
    public ArrayList<String> getLatestCommits(String branchName) {
        System.out.println("checkIfNewVersionIsAvailable");
        //https://github.com/centic9/jgit-cookbook/blob/master/src/main/java/org/dstadler/jgit/porcelain/ShowLog.java
        ArrayList<String> commitMessages = new ArrayList<>();
        Git git = null;
        try {
            git = Git.open(new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE + "/.git"));
            Repository repository = git.getRepository();
            Ref head = repository.getAllRefs().get("HEAD");
            String lastLocalCommitId = head.getObjectId().getName();
            git.fetch().call();
            Iterable<RevCommit> logs = git.log().call();
            String resolvedRepository = "remotes/origin/" + branchName;
            //TODO ADD SOURCE
            logs = git.log()
                    //.add(repository.resolve("remotes/origin/test"))
                    .add(repository.resolve(resolvedRepository))
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
            System.out.println("getLatestCommits failed Ohoh");
            System.out.println(e);
        }
        return commitMessages;
    }

    public void updateRepository(String repoUrl) throws IOException, GitAPIException {
        Git git;
        //https://download.eclipse.org/jgit/site/5.6.0.201912101111-r/apidocs/org/eclipse/jgit/api/Git.html#open-java.io.File-
        git = Git.open(new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE));
        git.pull().call();
    }
}
