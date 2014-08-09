package nl.tranquilizedquality.adm.security.business.generator;

import java.util.Random;

/**
 * Simple random password generator.
 * 
 * @author Salomo Petrus (salomo.petrus@tr-quality.com)
 * @since Aug 25, 2012
 * 
 */
public class SimplePasswordGenerator implements PasswordGenerator {

	/** The randomizer. */
	private final Random rgen = new Random();

	/** Decision byte to decide which value to pick. */
	private byte decision;

	/** Numeric value. */
	private byte numValue;

	/** Character value. */
	private char charValue;

	@Override
	public String generate(final int length) {
		final StringBuilder sb = new StringBuilder();

		while (sb.length() < length) {
			decision = (byte) rgen.nextInt(2);
			numValue = (byte) rgen.nextInt(10);
			charValue = (char) (rgen.nextInt(25) + 65);
			sb.append(decision % 2 == 0 ? charValue + "" : numValue + "");
		}

		return sb.toString();
	}

}
