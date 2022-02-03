import static org.junit.Assert.*;
import java.util.Collection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SocialNetworkTest {

	SocialNetwork sn;
	Account me;
	
	@Before
	public void setUp() {
		sn = new SocialNetwork();
		me = sn.join("Hakan");
		sn.login(me);
	}

	@After
	public void tearDown() throws Exception {
	
	}

	@Test 
	public void loggingInAssignsCurrentUser() throws NoUserLoggedInException {
		sn.login(me);
		assertEquals(sn.getCurrentUser(), me);
	}

	@Test(expected = NoUserLoggedInException.class)
	public void getCurrentUserThrowsExceptionWhenNoUserIsLoggedIn() throws NoUserLoggedInException {
		sn.login(null);
		sn.getCurrentUser();
	}

	@Test(expected = NoUserLoggedInException.class)
	public void sendFriendshipToThrowsExceptionWhenNoUserIsLoggedIn() throws NoUserLoggedInException {
		sn.login(null);
		sn.sendFriendshipTo("RandomUser");
	}

	@Test(expected = NoUserLoggedInException.class)
	public void blockThrowsExceptionWhenNoUserIsLoggedIn() throws NoUserLoggedInException {
		sn.login(null);
		sn.block("RandomUser");
	}

	@Test(expected = NoUserLoggedInException.class)
	public void unblockThrowsExceptionWhenNoUserIsLoggedIn() throws NoUserLoggedInException {
		sn.login(null);
		sn.unblock("RandomUser");
	}

	@Test(expected = NoUserLoggedInException.class)
	public void sendFriendshipCancellationToThrowsExceptionWhenNoUserIsLoggedIn() throws NoUserLoggedInException {
		sn.login(null);
		sn.sendFriendshipCancellationTo("RandomUser");
	}

	@Test(expected = NoUserLoggedInException.class)
	public void acceptFriendshipFromThrowsExceptionWhenNoUserIsLoggedIn() throws NoUserLoggedInException {
		sn.login(null);
		sn.acceptFriendshipFrom("RandomUser");
	}

	@Test(expected = NoUserLoggedInException.class)
	public void acceptAllFriendshipsThrowsExceptionWhenNoUserIsLoggedIn() throws NoUserLoggedInException {
		sn.login(null);
		sn.acceptAllFriendships();
	}

	@Test(expected = NoUserLoggedInException.class)
	public void rejectFriendshipFromThrowsExceptionWhenNoUserIsLoggedIn() throws NoUserLoggedInException {
		sn.login(null);
		sn.rejectFriendshipFrom("RandomUser");
	}

	@Test(expected = NoUserLoggedInException.class)
	public void rejectAllFriendshipsThrowsExceptionWhenNoUserIsLoggedIn() throws NoUserLoggedInException {
		sn.login(null);
		sn.rejectAllFriendships();
	}

	@Test(expected = NoUserLoggedInException.class)
	public void autoAcceptFriendshipsThrowsExceptionWhenNoUserIsLoggedIn() throws NoUserLoggedInException {
		sn.login(null);
		sn.autoAcceptFriendships();
	}

	@Test(expected = NoUserLoggedInException.class)
	public void cancelAutoAcceptFriendshipsThrowsExceptionWhenNoUserIsLoggedIn() throws NoUserLoggedInException {
		sn.login(null);
		sn.cancelAutoAcceptFriendships();
	}

	@Test(expected = NoUserLoggedInException.class)
	public void recommendFriendsThrowsExceptionWhenNoUserIsLoggedIn() throws NoUserLoggedInException {
		sn.login(null);
		sn.recommendFriends();
	}

	@Test(expected = NoUserLoggedInException.class)
	public void leaveThrowsExceptionWhenNoUserIsLoggedIn() throws NoUserLoggedInException {
		sn.login(null);
		sn.leave();
	}

	@Test 
	public void hasMemberDetectsUser() {
		assertTrue(sn.hasMember(me.getUserName()));
	}

	@Test 
	public void hasMemberDetectsNoValidUser() {
		assertFalse(sn.hasMember("NotAUserName"));
	}

	@Test
	public void autoAcceptThenCancelAutoAcceptFriendship()  throws NoUserLoggedInException {
		sn.autoAcceptFriendshipsTo(me);
		sn.cancelAutoAcceptFriendships();
		Account Aditi = sn.join("Aditi");
		sn.sendFriendshipTo("Hakan", Aditi);
		assertFalse(me.hasFriend("Aditi"));
		assertFalse(Aditi.hasFriend("Hakan"));
	}

	@Test
	public void recommendUsersWithTwoCommonFriends() throws NoUserLoggedInException {
		Account Cecile = sn.join("Cecile");
		Account Aditi = sn.join("Aditi");
		Account Joe = sn.join("Joe");
		sn.sendFriendshipTo("Hakan", Cecile);
		sn.sendFriendshipTo("Hakan", Aditi);
		sn.sendFriendshipTo("Aditi", Cecile);
		sn.sendFriendshipTo("Cecile", Aditi);
		sn.sendFriendshipTo("Joe", Cecile);
		sn.sendFriendshipTo("Joe", Aditi);
		sn.acceptAllFriendshipsTo(me);
		sn.acceptAllFriendshipsTo(Cecile);
		sn.acceptAllFriendshipsTo(Aditi);
		sn.acceptAllFriendshipsTo(Joe);
		assertTrue(sn.recommendFriends().contains("Joe"));
	}

	@Test
	public void doNotRecommendUsersWithLessThanTwoCommonFriends() throws NoUserLoggedInException {
		Account Cecile = sn.join("Cecile");
		Account Aditi = sn.join("Aditi");
		Account Joe = sn.join("Joe");
		sn.sendFriendshipTo("Hakan", Cecile);
		sn.sendFriendshipTo("Hakan", Aditi);
		sn.sendFriendshipTo("Aditi", Cecile);
		sn.sendFriendshipTo("Cecile", Aditi);
		sn.sendFriendshipTo("Joe", Cecile);
		sn.acceptAllFriendshipsTo(me);
		sn.acceptAllFriendshipsTo(Cecile);
		sn.acceptAllFriendshipsTo(Aditi);
		sn.acceptAllFriendshipsTo(Joe);
		assertFalse(sn.recommendFriends().contains("Joe"));
	}

	@Test 
	public void blockUserFromSeeingMeOnList() throws NoUserLoggedInException {
		Account her = sn.join("Cecile");
		sn.block("Cecile");
		sn.login(her);
		assertFalse(sn.listMembers().contains("Hakan"));
	}

	@Test 
	public void blockUserFromSendingFriendRequests() throws NoUserLoggedInException {
		Account her = sn.join("Cecile");
		sn.block("Cecile");
		sn.login(her);
		sn.sendFriendshipTo("hakan");
		assertFalse(her.getIncomingRequests().contains("Hakan"));
	}

	@Test 
	public void removePendingIncomingRequestsWhenBlockingUser() throws NoUserLoggedInException {
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("hakan", her);
		sn.block("Cecile");
		assertFalse(her.getIncomingRequests().contains("Hakan"));
	}

	@Test 
	public void removePendingOutgoingRequestsWhenBlockingUser() throws NoUserLoggedInException {
		sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.block("Cecile");
		assertFalse(me.getOutgoingRequests().contains("Cecile"));
	}

	@Test 
	public void blockThenUnblockUserFromSeeingMeOnList() throws NoUserLoggedInException {
		Account her = sn.join("Cecile");
		sn.block("Cecile");
		sn.unblock("Cecile");
		sn.login(her);
		assertTrue(sn.listMembers().contains("Hakan"));
	}

	@Test 
	public void unblockUserWhoWasNeverBlocked() throws NoUserLoggedInException{
		Account her = sn.join("Cecile");
		sn.unblock("Cecile");
		sn.login(her);
		assertTrue(sn.listMembers().contains("Hakan"));
	}

	@Test 
	public void accountIsCreated() {
		assertNotNull(me);
		assertEquals("Hakan", me.getUserName());
	}

	@Test 
	public void sameUsernameCannotBeUsedTwice() {
		Account nullMe = sn.join("Hakan");
		assertNull(nullMe);
	}

	@Test 
	public void nullUsernameCannotBeUsed() {
		Account nullMe = sn.join(null);
		assertNull(nullMe);
	}

	@Test 
	public void emptyUsernameCannotBeUsed() {
		Account nullMe = sn.join("");
		assertNull(nullMe);
	}
	
	@Test 
	public void canListSingleMemberOfSocialNetworkAfterOnePersonJoining() {
		Collection<String> members = sn.listMembers();
		assertTrue(members.contains("Hakan"));
	}

	@Test 
	public void sizeOfNetworkEqualsOneAfterOnePersonJoining() {
		Collection<String> members = sn.listMembers();
		assertEquals(1, members.size());
	}

	@Test 
	public void canListTwoPeopleWhoJoinSocialNetwork() {
		sn.join("Cecile");
		Collection<String> members = sn.listMembers();
		assertTrue(members.contains("Hakan"));
		assertTrue(members.contains("Cecile"));
	}
	
	@Test 
	public void sizeOfNetworkEqualsTwoAfterTwoPeopleJoinSocialNetwork() {
		sn.join("Cecile");
		Collection<String> members = sn.listMembers();
		assertEquals(2, members.size());
	}
	
	@Test 
	public void sendFriendRequest() {
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		assertTrue(her.getIncomingRequests().contains("Hakan"));
	}

	@Test 
	public void sendFriendRequestToInterface() throws NoUserLoggedInException {
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile");
		assertTrue(her.getIncomingRequests().contains("Hakan"));
	}

	@Test 
	public void acceptFriendRequest() {
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.acceptFriendshipFrom("Hakan", her);
		assertTrue(me.hasFriend("Cecile"));
		assertTrue(her.hasFriend("Hakan"));
	}

	@Test 
	public void acceptFriendRequestInterface() throws NoUserLoggedInException {
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.login(her);
		sn.acceptFriendshipFrom("Hakan");
		assertTrue(me.hasFriend("Cecile"));
		assertTrue(her.hasFriend("Hakan"));
	}

	@Test 
	public void dontAcceptFriendRequestBeforeOneIsSent() {
		Account her = sn.join("Cecile");
		sn.acceptFriendshipFrom("Hakan", her);
		assertFalse(me.hasFriend("Cecile"));
		assertFalse(her.hasFriend("Hakan"));
	}

	@Test 
	public void rejectFriendRequest() {
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.rejectFriendshipFrom("Hakan", her);
		assertFalse(me.getOutgoingRequests().contains("Cecile"));
		assertFalse(her.getIncomingRequests().contains("Hakan"));
	}

	@Test 
	public void rejectFriendRequestInterface() throws NoUserLoggedInException {
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.login(her);
		sn.rejectFriendshipFrom("Hakan");
		assertFalse(me.getOutgoingRequests().contains("Cecile"));
		assertFalse(her.getIncomingRequests().contains("Hakan"));
	}

	@Test 
	public void cancelFriendship() {
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.acceptFriendshipFrom("Hakan", her);
		sn.sendFriendshipCancellationTo("Cecile", me);
		assertFalse(me.hasFriend("Cecile"));
		assertFalse(her.hasFriend("Hakan"));
	}

	@Test
	public void cancelFriendshipViaInterface() throws NoUserLoggedInException {
		Account her = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.acceptFriendshipFrom("Hakan", her);
		sn.sendFriendshipCancellationTo("Cecile");
		assertFalse(me.hasFriend("Cecile"));
		assertFalse(her.hasFriend("Hakan"));
	}

	@Test
	public void sendFriendRequestToNonexistantUserDoesNotAddToOutgoingRequests() {
		sn.sendFriendshipTo("Cecile", me);
		assertFalse(me.getOutgoingRequests().contains("Cecile"));
	}

	@Test
	public void sendFriendRequestAddsAnOutgoingResponse() {
		sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		assertTrue(me.getOutgoingRequests().contains("Cecile"));
	}

	@Test
	public void testAcceptAllFriendships() {
		Account Cecile = sn.join("Cecile");
		Account Aditi = sn.join("Aditi");
		sn.sendFriendshipTo("Hakan", Cecile);
		sn.sendFriendshipTo("Hakan", Aditi);
		sn.acceptAllFriendshipsTo(me);
		assertTrue(me.hasFriend("Cecile"));
		assertTrue(me.hasFriend("Aditi"));
		assertTrue(Cecile.hasFriend("Hakan"));
		assertTrue(Aditi.hasFriend("Hakan"));
	}

	@Test
	public void testAcceptAllFriendshipsInterface() throws NoUserLoggedInException {
		Account Cecile = sn.join("Cecile");
		Account Aditi = sn.join("Aditi");
		sn.sendFriendshipTo("Hakan", Cecile);
		sn.sendFriendshipTo("Hakan", Aditi);
		sn.acceptAllFriendships();
		assertTrue(me.hasFriend("Cecile"));
		assertTrue(me.hasFriend("Aditi"));
		assertTrue(Cecile.hasFriend("Hakan"));
		assertTrue(Aditi.hasFriend("Hakan"));
	}

	@Test
	public void testRejectAllFriendships() {
		Account Cecile = sn.join("Cecile");
		Account Aditi = sn.join("Aditi");
		sn.sendFriendshipTo("Hakan", Cecile);
		sn.sendFriendshipTo("Hakan", Aditi);
		sn.rejectAllFriendshipsTo(me);
		assertFalse(me.getIncomingRequests().contains("Cecile"));
		assertFalse(me.getIncomingRequests().contains("Aditi"));
		assertFalse(Cecile.getOutgoingRequests().contains("Hakan"));
		assertFalse(Aditi.getOutgoingRequests().contains("Hakan"));
	}

	@Test
	public void testRejectAllFriendshipsInterface() throws NoUserLoggedInException {
		Account Cecile = sn.join("Cecile");
		Account Aditi = sn.join("Aditi");
		sn.sendFriendshipTo("Hakan", Cecile);
		sn.sendFriendshipTo("Hakan", Aditi);
		sn.rejectAllFriendships();
		assertFalse(me.getIncomingRequests().contains("Cecile"));
		assertFalse(me.getIncomingRequests().contains("Aditi"));
		assertFalse(Cecile.getOutgoingRequests().contains("Hakan"));
		assertFalse(Aditi.getOutgoingRequests().contains("Hakan"));
	}

	@Test
	public void testAutoAcceptAllFriendships() {
		sn.autoAcceptFriendshipsTo(me);
		Account Cecile = sn.join("Cecile");
		Account Aditi = sn.join("Aditi");
		sn.sendFriendshipTo("Hakan", Cecile);
		sn.sendFriendshipTo("Hakan", Aditi);
		assertTrue(me.hasFriend("Cecile"));
		assertTrue(me.hasFriend("Aditi"));
		assertTrue(Cecile.hasFriend("Hakan"));
		assertTrue(Aditi.hasFriend("Hakan"));
	}

	@Test
	public void testAutoAcceptAllFriendshipsInterface() throws NoUserLoggedInException {
		sn.autoAcceptFriendships();
		Account Cecile = sn.join("Cecile");
		Account Aditi = sn.join("Aditi");
		sn.sendFriendshipTo("Hakan", Cecile);
		sn.sendFriendshipTo("Hakan", Aditi);
		assertTrue(me.hasFriend("Cecile"));
		assertTrue(me.hasFriend("Aditi"));
		assertTrue(Cecile.hasFriend("Hakan"));
		assertTrue(Aditi.hasFriend("Hakan"));
	}

	@Test
	public void testAutoAcceptSingleFriendship() {
		sn.autoAcceptFriendshipsTo(me);
		Account Cecile = sn.join("Cecile");
		sn.sendFriendshipTo("Hakan", Cecile);
		assertTrue(me.hasFriend("Cecile"));
		assertTrue(Cecile.hasFriend("Hakan"));
	}

	@Test
	public void testRemoveMeFromOnePersonSocialNetwork() {
		sn.leave(me);
		assertTrue(sn.listMembers().isEmpty());
	}

	@Test
	public void leaveOnePersonSocialNetwork() throws NoUserLoggedInException {
		sn.leave();
		assertTrue(sn.listMembers().isEmpty());
	}

	@Test
	public void removeMeFromSocialNetworkWithTwoFriendsRemovesMeFromMyFriendsRecords() {
		me.autoAcceptFriendships();
		Account Cecile = sn.join("Cecile");
		Account Aditi = sn.join("Aditi");
		sn.sendFriendshipTo("Hakan", Cecile);
		sn.sendFriendshipTo("Hakan", Aditi);
		sn.leave(me);
		assertFalse(Cecile.hasFriend("Hakan"));
		assertFalse(Aditi.hasFriend("Hakan"));
	}

	@Test
	public void removeMeFromSocialNetworkIncludingFromOthersOutgoingPendingRequests() {
		Account Cecile = sn.join("Cecile");
		sn.sendFriendshipTo("Hakan", Cecile);
		sn.leave(me);
		assertFalse(Cecile.getOutgoingRequests().contains("Hakan"));
	}

	@Test
	public void removeMeFromSocialNetworkIncludingFromOthersIncomingPendingRequests() {
		Account Cecile = sn.join("Cecile");
		sn.sendFriendshipTo("Cecile", me);
		sn.leave(me);
		assertFalse(Cecile.getIncomingRequests().contains("Hakan"));
	}
}
