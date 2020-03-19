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
    private static DownloadTask single_instance = null;
    private static Git clone;
    private static String BRANCH_HEAD = "HEAD";
    private String BRANCH_ORIGIN = "remotes/origin/";

    public static DownloadTask getInstance() {
        if (single_instance == null) {
            single_instance = new DownloadTask();
        }
        return single_instance;
    }

    private DownloadTask() { }

    //https://www.vogella.com/tutorials/JGit/article.html
    public void cloneRepository(String repoUrl, File contentRepoFile, String branchPath) throws IOException {
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
            }
        }
    }

    //get commit messages ahead of local repository
    public ArrayList<String> getLatestCommits(String branchName) {
        ArrayList<String> commitMessages = new ArrayList<>();
        Git git = null;
        try {
            git = Git.open(new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE + "/.git"));
            Repository repository = git.getRepository();
            //https://stackoverflow.com/a/33120428
            Ref head = repository.getAllRefs().get(BRANCH_HEAD);
            String lastLocalCommitId = head.getObjectId().getName();
            git.fetch().call();
            Iterable<RevCommit> logs;
            //https://www.baeldung.com/jgit#4-logcommand-git-log
            String resolvedRepository = BRANCH_ORIGIN + branchName;
            logs = git.log()
                    .add(repository.resolve(resolvedRepository))
                    .call();
            for (RevCommit revCommit : logs) {
                String currentRevCommitID = revCommit.getId().getName();
                if (currentRevCommitID.equals(lastLocalCommitId)) {
                    break;
                }
                commitMessages.add(revCommit.getFullMessage());
            }
            repository.close();
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
        return commitMessages;
    }

    //pull repo
    public void updateRepository() throws IOException, GitAPIException {
        Git git;
        //https://download.eclipse.org/jgit/site/5.6.0.201912101111-r/apidocs/org/eclipse/jgit/api/Git.html#open-java.io.File-
        git = Git.open(new File(RepoLocalStorageDataProvider.getUserProjectDirectory() + LOCAL_STORAGE_FILE + REPO_LOCAL_STORAGE_FILE));
        git.pull().call();
    }
}
