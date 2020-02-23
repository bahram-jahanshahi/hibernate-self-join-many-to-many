package ir.afarinesh.mctab.hibernateselfjoinmanytomany.usecases;

import ir.afarinesh.mctab.hibernateselfjoinmanytomany.core.annotations.UseCase;
import ir.afarinesh.mctab.hibernateselfjoinmanytomany.entities.User;

@UseCase
public interface AddFollowersForUserUseCase {
    Boolean addFollowers(User user, User... followers) throws Exception;
}
