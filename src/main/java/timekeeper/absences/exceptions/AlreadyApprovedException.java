package timekeeper.absences.exceptions;

public class AlreadyApprovedException extends RuntimeException {
  private static final long serialVersionUID = 615270450776901223L;

  public AlreadyApprovedException(final String message) {
    super(message);
  }
}
