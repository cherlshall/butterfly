package com.cherlshall.butterfly.user.dao;

import com.atlassian.crowd.integration.rest.service.factory.RestCrowdClientFactory;
import com.atlassian.crowd.service.client.CrowdClient;
import com.cherlshall.butterfly.common.exception.ButterflyException;
import com.cherlshall.butterfly.common.vo.Code;
import com.cherlshall.butterfly.util.auth.Crowd;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by htf on 2020/9/25.
 */
@Repository
public class CrowdAccountDaoImpl implements AccountDao {

    @Value("${auth.crowd.url}")
    private String url;

    @Value("${auth.crowd.applicationName}")
    private String applicationName;

    @Value("${auth.crowd.applicationPassword}")
    private String applicationPassword;

    @Override
    public List<String> getAuthority(String userName, String password) {
        List<String> defaultUserAuth = Crowd.defaultUser(userName, password);
        if (defaultUserAuth != null) {
            return defaultUserAuth;
        }
        CrowdClient client = new RestCrowdClientFactory().newInstance(url, applicationName, applicationPassword);
        try {
            client.authenticateUser(userName, password);
            List<String> groups = client.getNamesOfGroupsForUser(userName, 0, 100);
            return Crowd.groupsToAuth(groups);
        } catch (Exception e) {
            throw new ButterflyException(Code.UNAUTH, e.getMessage());
        }
    }

}
