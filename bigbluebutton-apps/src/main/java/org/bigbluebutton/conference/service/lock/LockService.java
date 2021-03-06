/**
* BigBlueButton open source conferencing system - http://www.bigbluebutton.org/
* 
* Copyright (c) 2012 BigBlueButton Inc. and by respective authors (see below).
*
* This program is free software; you can redistribute it and/or modify it under the
* terms of the GNU Lesser General Public License as published by the Free Software
* Foundation; either version 3.0 of the License, or (at your option) any later
* version.
* 
* BigBlueButton is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
* PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License along
* with BigBlueButton; if not, see <http://www.gnu.org/licenses/>.
*
*/
package org.bigbluebutton.conference.service.lock;

import java.util.ArrayList;
import java.util.Map;
import org.bigbluebutton.conference.BigBlueButtonSession;
import org.bigbluebutton.conference.Constants;
import org.bigbluebutton.core.api.IBigBlueButtonInGW;
import org.red5.logging.Red5LoggerFactory;
import org.red5.server.api.Red5;
import org.slf4j.Logger;

public class LockService {
	private static Logger log = Red5LoggerFactory.getLogger( LockService.class, "bigbluebutton" );
	
	private IBigBlueButtonInGW bbbInGW;

	/**
	 * Internal function used to get the session
	 * */
	private BigBlueButtonSession getBbbSession() {
		return (BigBlueButtonSession) Red5.getConnectionLocal().getAttribute(Constants.SESSION);
	}
	
	public void setBigBlueButtonInGW(IBigBlueButtonInGW inGW) {
		bbbInGW = inGW;
	}
	
	/**
	 * Called from client to get lock settings for this room. 
	 * */
	public void getLockSettings(){
		String meetingId = getBbbSession().getRoom();
		String userId = getMyUserId();
		bbbInGW.getLockSettings(meetingId, userId);
	}
	
	/**
	 * Called from client to get lock settings for this room.
	 * 
	 * If room don't have any lock settings, it applies the defaultSettings 
	 * sent from client (came from config.xml).
	 *
	 * Returns the new lock settings for the room. 
	 * */
	public void setLockSettings(Map<String, Boolean> newSettings){
		String meetingId = getBbbSession().getRoom();
		String userId = getMyUserId();
		bbbInGW.sendLockSettings(meetingId, userId, newSettings);
	}
	
	/**
	 * Method called from client on connect to know if the room is locked or not 
	 * */
	public void isRoomLocked(){
		String meetingId = getBbbSession().getRoom();
		String userId = getMyUserId();
		bbbInGW.isMeetingLocked(meetingId, userId);
	}
	
	/**
	 * This method locks (or unlocks), based on lock parameter  
	 * all users but the users listed in array dontLockTheseUsers
	 * */
	public void setAllUsersLock(Boolean lock, ArrayList<String> dontLockTheseUsers){
		log.debug("setAllUsersLock ({}, {})", new Object[] { lock, dontLockTheseUsers });
	
	}
	
	/**
	 * This method locks or unlocks a specific user
	 * */
	public void setUserLock(Boolean lock, String internalUserID){
		log.debug("setUserLock ({}, {}, {})", new Object[] { lock, internalUserID });
		String meetingId = getBbbSession().getRoom();
		bbbInGW.lockUser(meetingId, lock, internalUserID);
	}
	
	public String getMyUserId() {
		BigBlueButtonSession bbbSession = (BigBlueButtonSession) Red5.getConnectionLocal().getAttribute(Constants.SESSION);
		assert bbbSession != null;
		return bbbSession.getInternalUserID();
	}
}
