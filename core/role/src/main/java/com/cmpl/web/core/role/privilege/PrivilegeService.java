package com.cmpl.web.core.role.privilege;

import java.util.List;

import com.cmpl.web.core.common.service.BaseService;

public interface PrivilegeService extends BaseService<PrivilegeDTO> {

  List<PrivilegeDTO> findByRoleId(String roleId);
}
