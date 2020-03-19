/*
 * This file is generated by jOOQ.
*/
package com.kevindeyne.tasker.jooq.tables.records;


import com.kevindeyne.tasker.jooq.tables.Issue;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record16;
import org.jooq.Row16;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class IssueRecord extends UpdatableRecordImpl<IssueRecord> implements Record16<Long, String, String, Long, Timestamp, String, Timestamp, String, Long, Long, String, String, String, Integer, Integer, Byte> {

    private static final long serialVersionUID = -1190318112;

    /**
     * Setter for <code>taskr.issue.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>taskr.issue.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>taskr.issue.title</code>.
     */
    public void setTitle(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>taskr.issue.title</code>.
     */
    public String getTitle() {
        return (String) get(1);
    }

    /**
     * Setter for <code>taskr.issue.description</code>.
     */
    public void setDescription(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>taskr.issue.description</code>.
     */
    public String getDescription() {
        return (String) get(2);
    }

    /**
     * Setter for <code>taskr.issue.assigned</code>.
     */
    public void setAssigned(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>taskr.issue.assigned</code>.
     */
    public Long getAssigned() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>taskr.issue.create_date</code>.
     */
    public void setCreateDate(Timestamp value) {
        set(4, value);
    }

    /**
     * Getter for <code>taskr.issue.create_date</code>.
     */
    public Timestamp getCreateDate() {
        return (Timestamp) get(4);
    }

    /**
     * Setter for <code>taskr.issue.create_user</code>.
     */
    public void setCreateUser(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>taskr.issue.create_user</code>.
     */
    public String getCreateUser() {
        return (String) get(5);
    }

    /**
     * Setter for <code>taskr.issue.update_date</code>.
     */
    public void setUpdateDate(Timestamp value) {
        set(6, value);
    }

    /**
     * Getter for <code>taskr.issue.update_date</code>.
     */
    public Timestamp getUpdateDate() {
        return (Timestamp) get(6);
    }

    /**
     * Setter for <code>taskr.issue.update_user</code>.
     */
    public void setUpdateUser(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>taskr.issue.update_user</code>.
     */
    public String getUpdateUser() {
        return (String) get(7);
    }

    /**
     * Setter for <code>taskr.issue.sprint_id</code>.
     */
    public void setSprintId(Long value) {
        set(8, value);
    }

    /**
     * Getter for <code>taskr.issue.sprint_id</code>.
     */
    public Long getSprintId() {
        return (Long) get(8);
    }

    /**
     * Setter for <code>taskr.issue.project_id</code>.
     */
    public void setProjectId(Long value) {
        set(9, value);
    }

    /**
     * Getter for <code>taskr.issue.project_id</code>.
     */
    public Long getProjectId() {
        return (Long) get(9);
    }

    /**
     * Setter for <code>taskr.issue.status</code>.
     */
    public void setStatus(String value) {
        set(10, value);
    }

    /**
     * Getter for <code>taskr.issue.status</code>.
     */
    public String getStatus() {
        return (String) get(10);
    }

    /**
     * Setter for <code>taskr.issue.impact</code>.
     */
    public void setImpact(String value) {
        set(11, value);
    }

    /**
     * Getter for <code>taskr.issue.impact</code>.
     */
    public String getImpact() {
        return (String) get(11);
    }

    /**
     * Setter for <code>taskr.issue.urgency</code>.
     */
    public void setUrgency(String value) {
        set(12, value);
    }

    /**
     * Getter for <code>taskr.issue.urgency</code>.
     */
    public String getUrgency() {
        return (String) get(12);
    }

    /**
     * Setter for <code>taskr.issue.workload</code>.
     */
    public void setWorkload(Integer value) {
        set(13, value);
    }

    /**
     * Getter for <code>taskr.issue.workload</code>.
     */
    public Integer getWorkload() {
        return (Integer) get(13);
    }

    /**
     * Setter for <code>taskr.issue.importance</code>.
     */
    public void setImportance(Integer value) {
        set(14, value);
    }

    /**
     * Getter for <code>taskr.issue.importance</code>.
     */
    public Integer getImportance() {
        return (Integer) get(14);
    }

    /**
     * Setter for <code>taskr.issue.overload</code>.
     */
    public void setOverload(Byte value) {
        set(15, value);
    }

    /**
     * Getter for <code>taskr.issue.overload</code>.
     */
    public Byte getOverload() {
        return (Byte) get(15);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record16 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row16<Long, String, String, Long, Timestamp, String, Timestamp, String, Long, Long, String, String, String, Integer, Integer, Byte> fieldsRow() {
        return (Row16) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row16<Long, String, String, Long, Timestamp, String, Timestamp, String, Long, Long, String, String, String, Integer, Integer, Byte> valuesRow() {
        return (Row16) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return Issue.ISSUE.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Issue.ISSUE.TITLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Issue.ISSUE.DESCRIPTION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field4() {
        return Issue.ISSUE.ASSIGNED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field5() {
        return Issue.ISSUE.CREATE_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return Issue.ISSUE.CREATE_USER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field7() {
        return Issue.ISSUE.UPDATE_DATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field8() {
        return Issue.ISSUE.UPDATE_USER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field9() {
        return Issue.ISSUE.SPRINT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field10() {
        return Issue.ISSUE.PROJECT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field11() {
        return Issue.ISSUE.STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field12() {
        return Issue.ISSUE.IMPACT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field13() {
        return Issue.ISSUE.URGENCY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field14() {
        return Issue.ISSUE.WORKLOAD;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field15() {
        return Issue.ISSUE.IMPORTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field16() {
        return Issue.ISSUE.OVERLOAD;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getTitle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component4() {
        return getAssigned();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component5() {
        return getCreateDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component6() {
        return getCreateUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component7() {
        return getUpdateDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component8() {
        return getUpdateUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component9() {
        return getSprintId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component10() {
        return getProjectId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component11() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component12() {
        return getImpact();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component13() {
        return getUrgency();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component14() {
        return getWorkload();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component15() {
        return getImportance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte component16() {
        return getOverload();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getTitle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getDescription();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value4() {
        return getAssigned();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value5() {
        return getCreateDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getCreateUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value7() {
        return getUpdateDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value8() {
        return getUpdateUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value9() {
        return getSprintId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value10() {
        return getProjectId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value11() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value12() {
        return getImpact();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value13() {
        return getUrgency();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value14() {
        return getWorkload();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value15() {
        return getImportance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value16() {
        return getOverload();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IssueRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IssueRecord value2(String value) {
        setTitle(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IssueRecord value3(String value) {
        setDescription(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IssueRecord value4(Long value) {
        setAssigned(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IssueRecord value5(Timestamp value) {
        setCreateDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IssueRecord value6(String value) {
        setCreateUser(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IssueRecord value7(Timestamp value) {
        setUpdateDate(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IssueRecord value8(String value) {
        setUpdateUser(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IssueRecord value9(Long value) {
        setSprintId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IssueRecord value10(Long value) {
        setProjectId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IssueRecord value11(String value) {
        setStatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IssueRecord value12(String value) {
        setImpact(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IssueRecord value13(String value) {
        setUrgency(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IssueRecord value14(Integer value) {
        setWorkload(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IssueRecord value15(Integer value) {
        setImportance(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IssueRecord value16(Byte value) {
        setOverload(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IssueRecord values(Long value1, String value2, String value3, Long value4, Timestamp value5, String value6, Timestamp value7, String value8, Long value9, Long value10, String value11, String value12, String value13, Integer value14, Integer value15, Byte value16) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        value11(value11);
        value12(value12);
        value13(value13);
        value14(value14);
        value15(value15);
        value16(value16);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached IssueRecord
     */
    public IssueRecord() {
        super(Issue.ISSUE);
    }

    /**
     * Create a detached, initialised IssueRecord
     */
    public IssueRecord(Long id, String title, String description, Long assigned, Timestamp createDate, String createUser, Timestamp updateDate, String updateUser, Long sprintId, Long projectId, String status, String impact, String urgency, Integer workload, Integer importance, Byte overload) {
        super(Issue.ISSUE);

        set(0, id);
        set(1, title);
        set(2, description);
        set(3, assigned);
        set(4, createDate);
        set(5, createUser);
        set(6, updateDate);
        set(7, updateUser);
        set(8, sprintId);
        set(9, projectId);
        set(10, status);
        set(11, impact);
        set(12, urgency);
        set(13, workload);
        set(14, importance);
        set(15, overload);
    }
}