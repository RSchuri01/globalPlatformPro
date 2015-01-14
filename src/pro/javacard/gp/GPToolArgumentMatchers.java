package pro.javacard.gp;

import joptsimple.ValueConversionException;
import joptsimple.ValueConverter;
import pro.javacard.gp.GPKeySet.GPKey;
import pro.javacard.gp.GPKeySet.GPKey.Type;
import pro.javacard.gp.GlobalPlatform.APDUMode;
import apdu4j.HexUtils;

public class GPToolArgumentMatchers {

	public static ValueConverter<AID> aid() {
		return new AIDMatcher();
	}

	public static class AIDMatcher implements ValueConverter<AID> {

		@Override
		public Class<AID> valueType() {
			return AID.class;
		}

		@Override
		public String valuePattern() {
			return null;
		}

		@Override
		public AID convert(String arg0) {
			try {
				return new AID(arg0);
			} catch (IllegalArgumentException e) {
				throw new ValueConversionException(arg0 + " is not a valid AID!");
			}
		}
	}

	public static ValueConverter<GPKey> key() {
		return new KeyMatcher();
	}

	public static class KeyMatcher implements ValueConverter<GPKey> {

		@Override
		public Class<GPKey> valueType() {
			return GPKey.class;
		}

		@Override
		public String valuePattern() {
			return null;
		}

		@Override
		public GPKey convert(String arg0) {
			try {
				String s = arg0.toLowerCase();
				if (s.startsWith("aes:")) {
					return new GPKey(HexUtils.decodeHexString(s.substring("aes:".length())), Type.AES);
				} else if (s.startsWith("des:")) {
					return new GPKey(HexUtils.decodeHexString(s.substring("des:".length())), Type.DES3);
				} else {
					// FIXME: not rally nice to fall back to 3DES, but works for 90% of usecases.
					return new GPKey(HexUtils.decodeHexString(arg0), Type.DES3);
				}
			} catch (IllegalArgumentException e) {
				throw new ValueConversionException(arg0 + " is not a valid key!");
			}
		}
	}

	public static ValueConverter<APDUMode> mode() {
		return new APDUModeMatcher();
	}

	public static class APDUModeMatcher implements ValueConverter<APDUMode> {

		@Override
		public Class<APDUMode> valueType() {
			return APDUMode.class;
		}

		@Override
		public String valuePattern() {
			return null;
		}

		@Override
		public APDUMode convert(String arg0) {
			try {
				return APDUMode.valueOf(arg0.trim().toUpperCase());
			} catch (IllegalArgumentException e) {
				throw new ValueConversionException(arg0 + " is not an APDU mode!");
			}
		}
	}
}