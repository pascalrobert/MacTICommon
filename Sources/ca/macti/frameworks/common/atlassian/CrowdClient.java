package ca.macti.frameworks.common.atlassian;

import java.util.List;

import com.atlassian.crowd.embedded.api.PasswordCredential;
import com.atlassian.crowd.embedded.api.User;
import com.atlassian.crowd.exception.ApplicationPermissionException;
import com.atlassian.crowd.exception.ExpiredCredentialException;
import com.atlassian.crowd.exception.GroupNotFoundException;
import com.atlassian.crowd.exception.InactiveAccountException;
import com.atlassian.crowd.exception.InvalidAuthenticationException;
import com.atlassian.crowd.exception.InvalidCredentialException;
import com.atlassian.crowd.exception.InvalidEmailAddressException;
import com.atlassian.crowd.exception.InvalidUserException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.exception.UserNotFoundException;
import com.atlassian.crowd.integration.rest.entity.PasswordEntity;
import com.atlassian.crowd.integration.rest.entity.UserEntity;
import com.atlassian.crowd.integration.rest.service.factory.RestCrowdClientFactory;
import com.atlassian.crowd.model.group.Group;
import com.atlassian.crowd.search.query.entity.restriction.NullRestrictionImpl;
import com.atlassian.crowd.search.query.entity.restriction.TermRestriction;
import com.atlassian.crowd.search.query.entity.restriction.constants.UserTermKeys;
import com.webobjects.foundation.NSArray;

public class CrowdClient {

  private com.atlassian.crowd.service.client.CrowdClient crowdClient;

  public CrowdClient(String crowdUrl, String appName, String appPassword) {
    crowdClient = new RestCrowdClientFactory().newInstance(crowdUrl,appName, appPassword);
  }

  public User authenticateUser(String username, String password) throws AccountException, ApplicationException {
    try {
      return crowdClient.authenticateUser(username, password);
    }
    catch (UserNotFoundException e) {
      throw new AccountException(e.getMessage(), e);
    }
    catch (InactiveAccountException e) {
      throw new AccountException(e.getMessage(), e);
    }
    catch (ExpiredCredentialException e) {
      throw new AccountException(e.getMessage(), e);
    }
    catch (ApplicationPermissionException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (InvalidAuthenticationException e) {
      throw new AccountException(e.getMessage(), e);
    }
    catch (OperationFailedException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
  }

  public void addUser(String userName, String firstName, String lastName, String fullName, String email, String password, boolean isActive) throws AccountException, ApplicationException  {
    User newUser = new UserEntity(userName, firstName, lastName, fullName, email, new PasswordEntity(password), isActive);
    try {
      crowdClient.addUser((com.atlassian.crowd.model.user.User) newUser, new PasswordCredential(password));
    }
    catch (ApplicationPermissionException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (InvalidAuthenticationException e) {
      throw new AccountException(e.getMessage(), e);
    }
    catch (OperationFailedException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (InvalidUserException e) {
      throw new AccountException(e.getMessage(), e);
    }
    catch (InvalidCredentialException e) {
      throw new AccountException(e.getMessage(), e);
    }  
  }

  public void addUserToGroup(String userName, String groupName) throws AccountException, ApplicationException {
    try {
      crowdClient.addUserToGroup(userName, groupName);
    }
    catch (ApplicationPermissionException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (InvalidAuthenticationException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (OperationFailedException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (GroupNotFoundException e) {
      throw new AccountException(e.getMessage(), e);
    }
    catch (UserNotFoundException e) {
      throw new AccountException(e.getMessage(), e);
    } 
  }

  public void removeUser(String userName) throws AccountException, ApplicationException {
    try {
      crowdClient.removeUser(userName);
    }
    catch (ApplicationPermissionException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (InvalidAuthenticationException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (OperationFailedException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (UserNotFoundException e) {
      throw new AccountException(e.getMessage(), e);
    } 
  }

  public void requestPasswordReset(String userName) throws AccountException, ApplicationException {
    try {
      crowdClient.requestPasswordReset(userName);
    }
    catch (ApplicationPermissionException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (InvalidAuthenticationException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (OperationFailedException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (UserNotFoundException e) {
      throw new AccountException(e.getMessage(), e);
    }
    catch (InvalidEmailAddressException e) {
      throw new AccountException(e.getMessage(), e);
    } 
  }
  
  public NSArray<String> findUsersByEmail(String email) throws AccountException, ApplicationException {
    try {
      java.util.List<String> userNames = crowdClient.searchUserNames(new TermRestriction<String>(UserTermKeys.EMAIL, email), 0, 1);
      return new NSArray<String>(userNames);
    }
    catch (ApplicationPermissionException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (InvalidAuthenticationException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (OperationFailedException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
  }
  
  public User findUserByUsername(String userName) throws AccountException, ApplicationException {
    try {
      return crowdClient.getUser(userName);
    }
    catch (ApplicationPermissionException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (InvalidAuthenticationException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (OperationFailedException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (UserNotFoundException e) {
      throw new AccountException(e.getMessage(), e);
    }
  }
  
  public void changePassword(String userName, String newPassword) throws AccountException, ApplicationException {
    try {
      crowdClient.updateUserCredential(userName, newPassword);
    }
    catch (ApplicationPermissionException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (InvalidAuthenticationException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (OperationFailedException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (UserNotFoundException e) {
      throw new AccountException(e.getMessage(), e);
    }
    catch (InvalidCredentialException e) {
      throw new AccountException(e.getMessage(), e);
    } 
  }
  
  public NSArray<User> listAllUsers() throws AccountException, ApplicationException {
    try {
      List<com.atlassian.crowd.model.user.User> users = crowdClient.searchUsers(NullRestrictionImpl.INSTANCE, 0, 2000);
      if ((users != null) && (users.size() > 0)) {
        return new NSArray<User>(users);
      }
    }
    catch (ApplicationPermissionException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (InvalidAuthenticationException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (OperationFailedException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    return new NSArray<User>();
  }
  
  public NSArray<Group> groupsForUser(String userName) throws AccountException, ApplicationException {
    try {
      List<com.atlassian.crowd.model.group.Group> groups = crowdClient.getGroupsForUser(userName, 0, -1);
      if ((groups != null) && (groups.size() > 0)) {
        return new NSArray<Group>(groups);
      }
    }
    catch (ApplicationPermissionException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (InvalidAuthenticationException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (OperationFailedException e) {
      throw new ApplicationException(e.getMessage(), e);
    }
    catch (UserNotFoundException e) {
      throw new AccountException(e.getMessage(), e);
    }
    return new NSArray<Group>();
  }
  
  public class AccountException extends Exception {

    public AccountException(String message, Throwable cause) {
      super(message, cause);
    }

  }

  public class ApplicationException extends Exception {

    public ApplicationException(String message, Throwable cause) {
      super(message, cause);
    }

  }

}
