import java.util.HashSet;
import java.util.Set;

public class SocialNetwork implements ISocialNetwork {
	
	private Set<Account> accounts = new HashSet<Account>();

	private Account currentUser = null;

	public Account login(Account me) {
		currentUser = me;
		return currentUser;
	}

	public Account getCurrentUser() throws NoUserLoggedInException {
		if (currentUser == null) {
			throw new NoUserLoggedInException();
		}
		return currentUser;
	}

	public boolean hasMember(String userName) {
		if (findAccountForUserName(userName) != null) {
			return true;
		}
		return false;
	}

	public void sendFriendshipTo(String userName) {
		sendFriendshipTo(userName, currentUser);
	}

	public void block(String userName) {
		currentUser.blockedUsers.add(userName);
		Account userAccount = findAccountForUserName(userName);
		wipeMeFromSingleMembersKnowledge(userAccount, currentUser);
	}

	public void unblock(String userName) {
		currentUser.blockedUsers.remove(userName);
	}

	public void sendFriendshipCancellationTo(String userName) {
		sendFriendshipCancellationTo(userName, currentUser);
	}

	public void acceptFriendshipFrom(String userName) {
		acceptFriendshipFrom(userName, currentUser);
	}

	public void acceptAllFriendships() {
		acceptAllFriendshipsTo(currentUser);
	}

	public void rejectFriendshipFrom(String userName) {

	}

	public void rejectAllFriendships() {

	}

	public void autoAcceptFriendships() {

	}

	public void cancelAutoAcceptFriendships() {
		if (currentUser != null) {
			currentUser.autoAcceptFriendRequests = false;
		}
	}

	public Set<String> recommendFriends() {
		Set<String> recommendedFriends = new HashSet<>();
		for (String member : listMembers()) {
			Account userAccount = findAccountForUserName(member);
			Set<String> membersFriends =  new HashSet<>(userAccount.getFriends());
			membersFriends.retainAll(currentUser.getFriends());
			if (membersFriends.size() > 1) {
				recommendedFriends.add(member);
			}
		}
		return recommendedFriends;
	}

	public void leave() {
		leave(currentUser);
	}

	// join SN with a new user name
	public Account join(String userName) {
		if (userName == null || userName == "" || listMembers().contains(userName)) {
			return null;
		}

		Account newAccount = new Account(userName);
		accounts.add(newAccount);
		return newAccount;
	}

	// find a member by user name 
	private Account findAccountForUserName(String userName) {
		if (!listMembers().contains(userName)) {  // return null if we are blocked by user
			return null;
		}
		// find account with user name userName
		// not accessible to outside because that would give a user full access to another member's account
		for (Account each : accounts) {
			if (each.getUserName().equals(userName)) 
					return each;
		}
		return null;
	}
	
	// list user names of all members
	public Set<String> listMembers() {
		Set<String> members = new HashSet<String>();
		for (Account each : accounts) {
			if (!each.blockedUsers.contains(currentUser.getUserName())){
				members.add(each.getUserName());
			}
		}
		return members;
	}
	
	// from my account, send a friend request to user with userName from my account
	public void sendFriendshipTo(String userName, Account me) {
		Account accountForUserName = findAccountForUserName(userName);
		if (accountForUserName != null) {
			accountForUserName.requestFriendship(me);
		}	
	}

	// from my account, cancel a friendship
	public void sendFriendshipCancellationTo(String userName, Account me) {
		Account accountForUserName = findAccountForUserName(userName);
		me.cancelFriendship(accountForUserName);
	}

	// from my account, do not accept a friend request if they did not ask
	public void acceptFriendshipFrom(String userName, Account me) {
		Account accountForUserName = findAccountForUserName(userName);
		accountForUserName.friendshipAccepted(me);
	}

	// from my account, accept all pending friend requests
	public void acceptAllFriendshipsTo(Account me) {
		Set<String> incomingRequests = new HashSet<>(me.getIncomingRequests());
		for (String friendToBe : incomingRequests) {
			acceptFriendshipFrom(friendToBe, me);
		}
	}

	// from my account, reject a pending friend request from another user with userName
	public void rejectFriendshipFrom(String userName, Account me) {
		Account accountForUserName = findAccountForUserName(userName);
		accountForUserName.friendshipRejected(me);
	}

	// from my account, accept all pending friend requests
	public void rejectAllFriendshipsTo(Account me) {
		Set<String> incomingRequests = new HashSet<>(me.getIncomingRequests());
		for (String friendNotToBe : incomingRequests) {
			rejectFriendshipFrom(friendNotToBe, me);
		}
	}

	public void autoAcceptFriendshipsTo(Account me) {
		me.autoAcceptFriendships();
	}

	// Remove my account from social network:
	public void leave(Account me) {
		Set<Account> accountsCopy = new HashSet<>(accounts);
		for (Account account : accountsCopy) {
			wipeMeFromSingleMembersKnowledge(account, me);
		}
		accounts.remove(me);
	}

	private void wipeMeFromSingleMembersKnowledge(Account user, Account me) {
		// They requested to be my friend
		if (user.getOutgoingRequests().contains(me.getUserName())) {
			user.friendshipAccepted(me);
		}
		// I requested to be their friend
		if (user.getIncomingRequests().contains(me.getUserName())) {
			me.friendshipAccepted(user);
		}
		// We are friends
		if (me.getFriends().contains(user.getUserName())) {
			sendFriendshipCancellationTo(user.getUserName(), me);
		}
	}
	
}
