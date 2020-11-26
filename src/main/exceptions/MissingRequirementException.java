package main.exceptions;

import java.util.List;

public class MissingRequirementException extends Exception {
    private final List<String> courseCodes;

    public MissingRequirementException(List<String> courseCodes) {
        super("Előzetes tárgykövetelmény nem teljesült.");
        this.courseCodes = courseCodes;
    }

    public List<String> getCourseCodes() {
        return courseCodes;
    }
}
