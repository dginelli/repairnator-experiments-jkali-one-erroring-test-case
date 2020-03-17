package za.co.absa.subatomic.domain.team;

import lombok.Value;

@Value
public class DevOpsEnvironmentRequested {

    private String teamId;

    private DevOpsEnvironment devOpsEnvironment;

    private TeamMemberId requestedBy;
}
