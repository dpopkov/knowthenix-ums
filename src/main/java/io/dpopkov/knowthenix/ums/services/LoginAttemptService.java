package io.dpopkov.knowthenix.ums.services;

public interface LoginAttemptService {

    // todo: public methods may use IP address instead of username

    /** Removes the entry of the specified user from the cache. */
    void evictUser(String username);

    /** Adds user's attempt and increments number of login attempts. */
    void addUserAttempt(String username);

    /** Checks whether the user reached the allowed number of attempts to login. */
    boolean reachedAttemptsLimit(String username);
}
