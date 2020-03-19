/*
 * This file is generated by jOOQ.
*/
package com.kevindeyne.tasker.jooq;


import com.kevindeyne.tasker.jooq.tables.Branch;
import com.kevindeyne.tasker.jooq.tables.Comments;
import com.kevindeyne.tasker.jooq.tables.Event;
import com.kevindeyne.tasker.jooq.tables.FlywaySchemaHistory;
import com.kevindeyne.tasker.jooq.tables.Invitation;
import com.kevindeyne.tasker.jooq.tables.Issue;
import com.kevindeyne.tasker.jooq.tables.Knowledge;
import com.kevindeyne.tasker.jooq.tables.Project;
import com.kevindeyne.tasker.jooq.tables.ProjectUsers;
import com.kevindeyne.tasker.jooq.tables.Releases;
import com.kevindeyne.tasker.jooq.tables.ReleasesChangelog;
import com.kevindeyne.tasker.jooq.tables.Search;
import com.kevindeyne.tasker.jooq.tables.Sprint;
import com.kevindeyne.tasker.jooq.tables.Tag;
import com.kevindeyne.tasker.jooq.tables.Tagcloud;
import com.kevindeyne.tasker.jooq.tables.Timesheet;
import com.kevindeyne.tasker.jooq.tables.User;
import com.kevindeyne.tasker.jooq.tables.UserRole;
import com.kevindeyne.tasker.jooq.tables.VersionIssue;
import com.kevindeyne.tasker.jooq.tables.Versions;

import javax.annotation.Generated;


/**
 * Convenience access to all tables in taskr
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>taskr.branch</code>.
     */
    public static final Branch BRANCH = com.kevindeyne.tasker.jooq.tables.Branch.BRANCH;

    /**
     * The table <code>taskr.comments</code>.
     */
    public static final Comments COMMENTS = com.kevindeyne.tasker.jooq.tables.Comments.COMMENTS;

    /**
     * The table <code>taskr.event</code>.
     */
    public static final Event EVENT = com.kevindeyne.tasker.jooq.tables.Event.EVENT;

    /**
     * The table <code>taskr.flyway_schema_history</code>.
     */
    public static final FlywaySchemaHistory FLYWAY_SCHEMA_HISTORY = com.kevindeyne.tasker.jooq.tables.FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY;

    /**
     * The table <code>taskr.invitation</code>.
     */
    public static final Invitation INVITATION = com.kevindeyne.tasker.jooq.tables.Invitation.INVITATION;

    /**
     * The table <code>taskr.issue</code>.
     */
    public static final Issue ISSUE = com.kevindeyne.tasker.jooq.tables.Issue.ISSUE;

    /**
     * The table <code>taskr.knowledge</code>.
     */
    public static final Knowledge KNOWLEDGE = com.kevindeyne.tasker.jooq.tables.Knowledge.KNOWLEDGE;

    /**
     * The table <code>taskr.project</code>.
     */
    public static final Project PROJECT = com.kevindeyne.tasker.jooq.tables.Project.PROJECT;

    /**
     * The table <code>taskr.project_users</code>.
     */
    public static final ProjectUsers PROJECT_USERS = com.kevindeyne.tasker.jooq.tables.ProjectUsers.PROJECT_USERS;

    /**
     * The table <code>taskr.releases</code>.
     */
    public static final Releases RELEASES = com.kevindeyne.tasker.jooq.tables.Releases.RELEASES;

    /**
     * The table <code>taskr.releases_changelog</code>.
     */
    public static final ReleasesChangelog RELEASES_CHANGELOG = com.kevindeyne.tasker.jooq.tables.ReleasesChangelog.RELEASES_CHANGELOG;

    /**
     * The table <code>taskr.search</code>.
     */
    public static final Search SEARCH = com.kevindeyne.tasker.jooq.tables.Search.SEARCH;

    /**
     * The table <code>taskr.sprint</code>.
     */
    public static final Sprint SPRINT = com.kevindeyne.tasker.jooq.tables.Sprint.SPRINT;

    /**
     * The table <code>taskr.tag</code>.
     */
    public static final Tag TAG = com.kevindeyne.tasker.jooq.tables.Tag.TAG;

    /**
     * The table <code>taskr.tagcloud</code>.
     */
    public static final Tagcloud TAGCLOUD = com.kevindeyne.tasker.jooq.tables.Tagcloud.TAGCLOUD;

    /**
     * The table <code>taskr.timesheet</code>.
     */
    public static final Timesheet TIMESHEET = com.kevindeyne.tasker.jooq.tables.Timesheet.TIMESHEET;

    /**
     * The table <code>taskr.user</code>.
     */
    public static final User USER = com.kevindeyne.tasker.jooq.tables.User.USER;

    /**
     * The table <code>taskr.user_role</code>.
     */
    public static final UserRole USER_ROLE = com.kevindeyne.tasker.jooq.tables.UserRole.USER_ROLE;

    /**
     * The table <code>taskr.versions</code>.
     */
    public static final Versions VERSIONS = com.kevindeyne.tasker.jooq.tables.Versions.VERSIONS;

    /**
     * The table <code>taskr.version_issue</code>.
     */
    public static final VersionIssue VERSION_ISSUE = com.kevindeyne.tasker.jooq.tables.VersionIssue.VERSION_ISSUE;
}