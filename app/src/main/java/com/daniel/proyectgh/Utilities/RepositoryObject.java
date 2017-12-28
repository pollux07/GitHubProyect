package com.daniel.proyectgh.Utilities;

/**
 * Created by daniel on 27/12/17.
 */

public class RepositoryObject {
    private String repoName;
    private String repoFullName;
    private String repoId;
    private String starsRepo;
    private String forksRepo;
    private String watchersRepo;

    public RepositoryObject(){}

    public String getRepoName() {
        return repoName;
    }

    public RepositoryObject setRepoName(String repo_name) {
        this.repoName = repo_name;

        return this;
    }

    public String getRepoFullName() {
        return repoFullName;
    }

    public RepositoryObject setRepoFullName(String repo_fullname) {
        this.repoFullName = repo_fullname;

        return this;
    }

    public String getRepoId() {
        return repoId;
    }

    public RepositoryObject setRepoId(String repo_id) {
        this.repoId = repo_id;

        return this;
    }

    public String getRepoStars() {
        return starsRepo;
    }

    public RepositoryObject setRepoStars(String repo_stars) {
        this.starsRepo = repo_stars;

        return this;
    }

    public String getRepoForks() {
        return forksRepo;
    }

    public RepositoryObject setRepoForks(String repo_forks) {
        this.forksRepo = repo_forks;

        return this;
    }

    public String getRepoWatchers() {
        return watchersRepo;
    }

    public RepositoryObject setRepoWatchers(String repo_watchers) {
        this.watchersRepo = repo_watchers;

        return this;
    }
}
