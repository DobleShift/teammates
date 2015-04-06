package teammates.ui.controller;

import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.exception.InvalidParametersException;
import teammates.common.util.Const;
import teammates.logic.api.GateKeeper;

public class AdminEmailTrashAction extends Action {

    @Override
    protected ActionResult execute() {
        
        new GateKeeper().verifyAdminPrivileges(account);
        
        String emailId = getRequestParamValue(Const.ParamsNames.ADMIN_EMAIL_ID);
        
        String redirect = getRequestParamValue(Const.ParamsNames.ADMIN_EMAIL_TRASH_ACTION_REDIRECT);
        redirect = redirect == null ? Const.ActionURIs.ADMIN_EMAIL_TRASH_PAGE : redirect;
        
        if(redirect.contains("sentpage")){
            redirect = Const.ActionURIs.ADMIN_EMAIL_SENT_PAGE;
        } else if (redirect.contains("draftpage")){
            redirect = Const.ActionURIs.ADMIN_EMAIL_DRAFT_PAGE;
        } else {
            redirect = Const.ActionURIs.ADMIN_EMAIL_TRASH_PAGE;
        }
        
        if(emailId == null || emailId.isEmpty()){
            statusToAdmin = "Invalid parameter : email id cannot be null or empty";
            statusToUser.add("Invalid parameter : email id cannot be null or empty");
            return createRedirectResult(redirect);     
        }
        
        if(requestUrl.contains(Const.ActionURIs.ADMIN_EMAIL_MOVE_TO_TRASH)){
            try {
                logic.moveAdminEmailToTrashBin(emailId);
                statusToAdmin = "Email with id" + emailId + " has been moved to trash bin";
                statusToUser.add("The item has been moved to trash bin");
            } catch (InvalidParametersException | EntityDoesNotExistException e) {
                setStatusForException(e, "An error has occurred when moving email to trash bin");
            }
            
            return createRedirectResult(redirect);   
            
        } else if(requestUrl.contains(Const.ActionURIs.ADMIN_EMAIL_MOVE_OUT_TRASH)){
            try {
                logic.moveAdminEmailOutOfTrashBin(emailId);
                statusToAdmin = "Email with id" + emailId + " has been moved out of trash bin";
                statusToUser.add("The item has been moved out of trash bin");
            } catch (InvalidParametersException | EntityDoesNotExistException e) {
                setStatusForException(e, "An error has occurred when moving email out of trash bin");
            }
            
            return createRedirectResult(Const.ActionURIs.ADMIN_EMAIL_TRASH_PAGE);   
        }
   
        return createRedirectResult(redirect);     
            
    }

}