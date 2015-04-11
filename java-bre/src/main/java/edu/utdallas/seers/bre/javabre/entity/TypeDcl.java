package edu.utdallas.seers.bre.javabre.entity;

public class TypeDcl {

	private String qualName;
	private TypeDclType type;
	private String name;

	public enum TypeDclType {
		CLASS, INTERFACE
	};

	public TypeDcl(String qualName, TypeDclType type, String name) {
		this.setQualName(qualName);
		this.setType(type);
		this.setName(name);
	}

	@Override
	public String toString() {
		return "TypeDcl [qualName=" + getQualName() + ", type=" + getType()
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getQualName() == null) ? 0 : getQualName().hashCode());
		result = prime * result
				+ ((getType() == null) ? 0 : getType().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TypeDcl other = (TypeDcl) obj;
		if (getQualName() == null) {
			if (other.getQualName() != null)
				return false;
		} else if (!getQualName().equals(other.getQualName()))
			return false;
		if (getType() != other.getType())
			return false;
		return true;
	}

	public TypeDclType getType() {
		return type;
	}

	public void setType(TypeDclType type) {
		this.type = type;
	}

	public String getQualName() {
		return qualName;
	}

	public void setQualName(String qualName) {
		this.qualName = qualName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
