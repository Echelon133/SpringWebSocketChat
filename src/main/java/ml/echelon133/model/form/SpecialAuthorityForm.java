package ml.echelon133.model.form;

import ml.echelon133.model.form.validator.ValidAuthority;

import javax.validation.constraints.NotEmpty;

public class SpecialAuthorityForm {

    @NotEmpty
    private String username;

    @ValidAuthority
    private String authority;

    public SpecialAuthorityForm() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
