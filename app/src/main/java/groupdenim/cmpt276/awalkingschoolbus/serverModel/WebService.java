package groupdenim.cmpt276.awalkingschoolbus.serverModel;

import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.userModel.GPSLocation;
import groupdenim.cmpt276.awalkingschoolbus.userModel.Group;
import groupdenim.cmpt276.awalkingschoolbus.userModel.Message;
import groupdenim.cmpt276.awalkingschoolbus.userModel.PermissionRequest;
import groupdenim.cmpt276.awalkingschoolbus.userModel.PermissionStatus;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Farhan on 2018-03-06.
 */

public interface WebService {

    String BASE_URL = "https://cmpt276-1177-bf.cmpt.sfu.ca:8443";
    String FEED_LOGIN = "/login";
    String FEED_REGISTER = "/users/signup";
    String FEED_GETUSERLIST = "/users";
    String FEED_GETGROUPBYID = "/groups/{id}";
    String FEED_GROUPS = "/groups";
    String FEED_GETUSERBYID = "/users/{id}";
    String FEED_GETUSERBYEMAIL = "/users/byEmail";
    String FEED_GETMONITORUSERS = "/users/{id}/monitorsUsers";
    String FEED_GETMONITOREDUSERS = "/users/{id}/monitoredByUsers";
    String FEED_MONITORUSER = "/users/{id}/monitorsUsers";
    String FEED_MONITOREDBYUSER = "/users/{id}/monitoredByUsers";
    String FEED_STOPMONITORING = "/users/{idA}/monitorsUsers/{idB}";
    String FEED_STOPBEINGMONITORED = "/users/{idA}/monitoredByUsers/{idB}";
    String FEED_DELETEGROUP = "/groups/{id}";
    String FEED_CREATEGROUP = "/groups";
    String FEED_EDITUSERBYID = "/users/{id}";
    String FEED_ADDTOGROUP = "/groups/{id}/memberUsers";
    String FEED_REMOVEFROMGROUP = "/groups/{groupId}/memberUsers/{userId}";
    String FEED_GETALLMESSAGES = "/messages";
    String FEED_GETMESSAGESFORUSER= "/messages";
    String FEED_SENDMESSAGETOPARENTS = "/messages/toparentsof/{userId}";
    String FEED_SENDMESSAGETOGROUPS = "/messages/togroup/{groupId}";
    String FEED_EDITREADSTATUS = "/messages/{messageId}/readby/{userId}";
    String FEED_SETLASTGPSLOCATION = "/users/{id}/lastGpsLocation";
    String FEED_GETLASTGPSLOCATION = "/users/{id}/lastGpsLocation";
    String FEED_GETPENDINGPERMISSION = "/permissions";
    String FEED_APPROVEORDENYPERMISSION = "/permissions/{Id}";
    String FEED_GETONEPERMISSION = "/permissions/{id}";
    String FEED_GETALLPERMISSIONSFORUSER= "/permissions";
    String APIKEY= "394ECE0B-5BF9-41C4-B9F6-261B0678ED23";


    //@Headers("apiKey: " + APIKEY)
    @POST(FEED_LOGIN)
    Call<Void> getLogin(@Body User userServer);

    //@Headers("apiKey: " + APIKEY)
    @POST(FEED_REGISTER)
    Call<User> registerUser(@Body User userServer);

    @GET(FEED_GETUSERLIST)
    Call<List<User>> getUserList();

    @GET(FEED_GETUSERBYID)
    Call<User> getUserById(@Path("id") Long userId);

    @GET(FEED_GETUSERBYEMAIL)
    Call<User> getUserByEmail(@Query("email") String email);


    @POST(FEED_EDITUSERBYID)
    Call<User> editUserById(@Path("id") long id,@Body User user);

    @GET(FEED_GETMONITORUSERS)
    Call<List<User>> getMonitorUser(@Path("id") Long id);

    @GET(FEED_GETMONITOREDUSERS)
    Call<List<User>> getMonitoredUser(@Path("id") Long id);

    @POST(FEED_MONITORUSER)
    Call<List<User>> monitorUser(
            @Path("id") long currentUserid,
            @Body User otherUser);

    @POST(FEED_MONITOREDBYUSER)
    Call<List<User>> addMonitoredBy(
            @Path("id") long otherUser,
            @Body User currentUser);

    @DELETE(FEED_STOPMONITORING)
    Call<Void> stopMonitoring(
            @Path("idA") long currentUser,
            @Path("idB")long otherUser);

    @DELETE(FEED_STOPBEINGMONITORED)
    Call<Void> stopBeingMonitored(
            @Path("idA") long currentUser,
            @Path("idB")long otherUser);

    @DELETE(FEED_DELETEGROUP)
    Call<Void> deleteGroup(@Path("id") long groupId);


    @POST(FEED_GROUPS)
    Call<Group> createNewGroup(@Body Group group);

    @GET(FEED_GROUPS)
    Call<List<Group>> getGroupList();

    @GET(FEED_GETGROUPBYID)
    Call<Group> getGroupById(@Path("id") Long groupId);

    @POST(FEED_GETGROUPBYID)
    Call<Group> updateGroupById(@Path("id") Long groupId,
                                @Body Group group);

    @POST(FEED_ADDTOGROUP)
    Call<List<User>> addNewMemberOfGroup(@Path("id") long groupId,
                                         @Body User userId);

    @DELETE(FEED_REMOVEFROMGROUP)
    Call<Void> removeMemberFromGroup(@Path("groupId") long groupId,
                                     @Path("userId") long userId);

    @GET(FEED_GETALLMESSAGES)
    Call<List<Message>> getAllMessages();

    @GET(FEED_GETMESSAGESFORUSER)
    Call<List<Message>> getMessageForUser(@Query("foruser") long id);

    @GET(FEED_GETMESSAGESFORUSER)
    Call<List<Message>> getMessageForUserReadOrUnread(@Query("foruser") long id, @Query("status") String unread);


    @POST(FEED_SENDMESSAGETOPARENTS)
    Call<Message> sendMessageToParents(@Path("userId") long id, @Body Message message);

    @POST(FEED_SENDMESSAGETOGROUPS)
    Call<Message> sendMessageToGroup(@Path("groupId") long id, @Body Message message);

    @POST(FEED_EDITUSERBYID)
    Call<User> editUserById(@Path("id") long id);

    @POST(FEED_EDITREADSTATUS)
    Call<User> editReadStatus(@Path("messageId") long idMessage, @Path("userId") long id, @Body boolean trueOrFalse);

    @POST(FEED_SETLASTGPSLOCATION)
    Call<GPSLocation> setLastGpsLocation(@Path("id") long id, @Body GPSLocation location);

    @GET(FEED_GETLASTGPSLOCATION)
    Call<GPSLocation> setLastGpsLocation(@Path("id") long id);


    @GET(FEED_GETPENDINGPERMISSION)
    Call<List<PermissionRequest>> getPendingPermission(@Query("userId") long id, @Query("statusForUser") String status);

    @POST(FEED_APPROVEORDENYPERMISSION)
    Call<PermissionRequest> approveOrDenyPermission(@Path("Id") long id, @Body String status);

    @GET(FEED_GETONEPERMISSION)
    Call<PermissionRequest> getOnePermission(@Path("id") long id);

    @GET(FEED_GETALLPERMISSIONSFORUSER)
    Call<List<PermissionRequest>> getAllPermissionForUsers(@Query("userId") long id);

}
