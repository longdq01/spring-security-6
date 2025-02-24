package com.example.spring_security_6.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@Data
public class UserInfoGithub {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "login")
    private String login;

    @JsonProperty(value = "node_id")
    private String nodeId;

    @JsonProperty(value = "avatar_url")
    private String avatarUrl;

    @JsonProperty(value = "gravatar_id")
    private String gravatarId;

    @JsonProperty(value = "url")
    private String url;

    @JsonProperty(value = "html_url")
    private String htmlUrl;

    @JsonProperty(value = "followers_url")
    private String followersUrl;

    @JsonProperty(value = "following_url")
    private String followingUrl;

    @JsonProperty(value = "gists_url")
    private String gistsUrl;

    @JsonProperty(value = "starred_url")
    private String starredUrl;

    @JsonProperty(value = "subscriptions_url")
    private String subscriptionsUrl;

    @JsonProperty(value = "organizations_url")
    private String organizationsUrl;

    @JsonProperty(value = "repos_url")
    private String reposUrl;

    @JsonProperty(value = "events_url")
    private String eventsUrl;

    @JsonProperty(value = "received_events_url")
    private String receivedEventsUrl;

    @JsonProperty(value = "type")
    private String type;

    @JsonProperty(value = "site_admin")
    private Boolean siteAdmin;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "company")
    private String company;

    @JsonProperty(value = "blog")
    private String blog;

    @JsonProperty(value = "location")
    private String location;

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "hireable")
    private Boolean hireable;

    @JsonProperty(value = "bio")
    private String bio;

    @JsonProperty(value = "twitter_username")
    private String twitterUsername;

    @JsonProperty(value = "public_repos")
    private Integer publicRepos;

    @JsonProperty(value = "public_gists")
    private Integer publicGists;

    @JsonProperty(value = "followers")
    private Integer followers;

    @JsonProperty(value = "following")
    private Integer following;

    @JsonProperty(value = "created_at")
    private Instant createdAt;

    @JsonProperty(value = "updated_at")
    private Instant updatedAt;

    @JsonProperty(value = "private_gists")
    private Integer privateGists;

    @JsonProperty(value = "total_private_repos")
    private Integer totalPrivateRepos;

    @JsonProperty(value = "owned_private_repos")
    private Integer ownedPrivateRepos;

    @JsonProperty(value = "disk_usage")
    private Integer diskUsage;

    @JsonProperty(value = "collaborators")
    private Integer collaborators;

    @JsonProperty(value = "two_factor_authentication")
    private Boolean twoFactorAuthentication;
}

