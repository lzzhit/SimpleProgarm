package android.telephony;

public abstract class CellInfo implements android.os.Parcelable {
	CellInfo() {
		throw new RuntimeException("Stub!");
	}

	public boolean isRegistered() {
		throw new RuntimeException("Stub!");
	}

	public long getTimeStamp() {
		throw new RuntimeException("Stub!");
	}

	public int hashCode() {
		throw new RuntimeException("Stub!");
	}

	public boolean equals(java.lang.Object other) {
		throw new RuntimeException("Stub!");
	}

	public java.lang.String toString() {
		throw new RuntimeException("Stub!");
	}

	public int describeContents() {
		throw new RuntimeException("Stub!");
	}

	public abstract void writeToParcel(android.os.Parcel dest, int flags);

	public static final android.os.Parcelable.Creator<android.telephony.CellInfo> CREATOR;
	static {
		CREATOR = null;
	}
}
