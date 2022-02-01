import java.util.HashSet;
import java.util.Set;
import java.util.Collection;

public class SocialNetwork {
	
	private Collection<Account> accounts = new HashSet<Account>();

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
		// find account with user name userName
		// not accessible to outside because that would give a user full access to another member's account
		for (Account each : accounts) {
			if (each.getUserName().equals(userName)) 
					return each;
		}
		return null;
	}
	
	// list user names of all members
	public Collection<String> listMembers() {
		Collection<String> members = new HashSet<String>();
		for (Account each : accounts) {
			members.add(each.getUserName());
		}
		return members;
	}
	
	// from my account, send a friend request to user with userName from my account
	public void sendFriendshipTo(String userName, Account me) {
		Account accountForUserName = findAccountForUserName(userName);
		if (accountForUserName != null) {
			accountForUserName.requestFriendship(me);

			if (accountForUserName.autoAcceptFriendRequests) {
				acceptFriendshipFrom(me.getUserName(), accountForUserName);
			}
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
		if (accountForUserName.getOutgoingRequests().contains(me.getUserName())) {
			accountForUserName.friendshipAccepted(me);
		}
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
		Collection<Account> accountsCopy = new HashSet<>(accounts);
		for (Account account : accountsCopy) {
			// They requested to be my friend
			if (account.getOutgoingRequests().contains(me.getUserName())) {
				account.friendshipAccepted(me);
			}
			// I requested to be their friend
			if (account.getIncomingRequests().contains(me.getUserName())) {
				me.friendshipAccepted(account);
			}
			// We are friends
			if (me.getFriends().contains(account.getUserName())) {
				sendFriendshipCancellationTo(account.getUserName(), me);
			}
			
		}
		accounts.remove(me);
	}
	
}
