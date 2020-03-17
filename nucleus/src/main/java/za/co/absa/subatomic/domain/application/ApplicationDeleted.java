package za.co.absa.subatomic.domain.application;

import lombok.Builder;
import lombok.Value;
import za.co.absa.subatomic.domain.pkg.ProjectId;
import za.co.absa.subatomic.domain.team.TeamMemberId;

@Value
@Builder
public class ApplicationDeleted {

    private String applicationId;
}
