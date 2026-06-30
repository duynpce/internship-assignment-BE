package org.example.authservice.application.usecase;

//implement this to for checking if the unique column is occupied
public interface ExistUseCase {
	boolean existsByUsername(String username);

	boolean existsByEmail(String email);
}
