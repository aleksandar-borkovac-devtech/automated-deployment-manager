package nl.tranquilizedquality.adm.security.business.generator;

/**
 * Interface for a password generator.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public interface PasswordGenerator {

	/**
	 * Generates a random password of the specified length.
	 * 
	 * @param length
	 *            The length the password will have.
	 * @return Returns the generated password as a {@link String} value.
	 */
	String generate(int length);

}
