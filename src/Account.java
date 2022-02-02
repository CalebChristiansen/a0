import java.util.HashSet;
import java.util.Set;


public class Account  {
	
	// the unique user name of account owner
	private String userName;
	
	// list of members who are awaiting an acceptance response from this account's owner 
	private Set<String> incomingRequests = new HashSet<String>();
	
	// list of members who are friends of this account's owner
	private Set<String> friends = new HashSet<String>();

	// list of members who are friends of this account's owner
	public Set<String> blockedUsers = new HashSet<String>();

	private Set<String> outGoingRequests = new HashSet<String>();

	public boolean autoAcceptFriendRequests = false;
	
	public Account(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	// return list of members who had sent a friend request to this account's owner 
	// and are still waiting for a response
	public Set<String> getIncomingRequests() {
		return incomingRequests; 
	}

	// an incoming friend request to this account's owner from another member account
	public void requestFriendship(Account fromAccount) {
		if (fromAccount == null) {
			return;
		}
		if (
			!friends.contains(fromAccount.getUserName()) &&  // we are not friends
			!getIncomingRequests().contains(fromAccount.getUserName()) &&  // incomming request does not exist
			!fromAccount.getOutgoingRequests().contains(getUserName()) // outgoing request does not exist
		) {
			incomingRequests.add(fromAccount.getUserName());
			fromAccount.addOutgoingRequest(getUserName());
			if (autoAcceptFriendRequests) {
				fromAccount.friendshipAccepted(this);
			}
		}
	}

	// check if account owner has a member with user name userName as a friend
	public boolean hasFriend(String userName) {
		return friends.contains(userName);
	}

	// receive an acceptance from a member to whom a friend request has been sent and from whom no response has been received
	public void friendshipAccepted(Account toAccount) {
		if (outGoingRequests.contains(toAccount.getUserName())) {
			friends.add(toAccount.getUserName());
			outGoingRequests.remove(toAccount.getUserName());
			toAccount.friends.add(this.getUserName());
			toAccount.incomingRequests.remove(this.getUserName());
		}
		
	}

	// receive an rejection from a member to whom a friend request has been sent and from whom no response has been received
	public void friendshipRejected(Account toAccount) {
		outGoingRequests.remove(toAccount.getUserName());
		toAccount.incomingRequests.remove(this.getUserName());
	}

	// Unfriend an existing friend
	public void cancelFriendship(Account toAccount) {
		friends.remove(toAccount.getUserName());
		toAccount.friends.remove(this.getUserName());
	}
	
	public Set<String> getFriends() {
		return friends;
	}

	// an outgoing friendship request from this user
	public void addOutgoingRequest(String futureFriend) {
		if (!outGoingRequests.contains(futureFriend)) {
			outGoingRequests.add(futureFriend);
		}
	}

	// return all outgoing requests for this user
	public Set<String> getOutgoingRequests() {
		return outGoingRequests;
	}

	public void autoAcceptFriendships() {
		autoAcceptFriendRequests = true;
	}
}
