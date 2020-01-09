/**
Copyright © 2019  Atos Spain SA. All rights reserved.
This file is part of SEAL Configuration Manager (SEAL ConfMngr).
SEAL ConfMngr is free software: you can redistribute it and/or modify it under the terms of EUPL 1.2.
THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT ANY WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT, 
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
See README file for the full disclaimer information and LICENSE file for full license information in the project root.
*/
package eu.seal.cm.rest_api.domain;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * AttributeMapPairings
 */
@Validated

public class AttributeMapPairings   {
  @JsonProperty("profile")
  private String profile = null;

  @JsonProperty("issuer")
  private String issuer = null;

  @JsonProperty("attributes")
  @Valid
  private List<String> attributes = null;

  @JsonProperty("regexp")
  private String regexp = null;

  @JsonProperty("replace")
  private String replace = null;

  public AttributeMapPairings profile(String profile) {
    this.profile = profile;
    return this;
  }

  /**
   * Attribute profile which this group belongs to
   * @return profile
  **/
  @ApiModelProperty(example = "eIDAS", value = "Attribute profile which this group belongs to")


  public String getProfile() {
    return profile;
  }

  public void setProfile(String profile) {
    this.profile = profile;
  }

  public AttributeMapPairings issuer(String issuer) {
    this.issuer = issuer;
    return this;
  }

  /**
   * ID of the entity that emmitted the attributes on this group. If specified, this equivalence will only apply to attributes coming from the correspondign issuer
   * @return issuer
  **/
  @ApiModelProperty(example = "http://clave.redsara.es/", value = "ID of the entity that emmitted the attributes on this group. If specified, this equivalence will only apply to attributes coming from the correspondign issuer")


  public String getIssuer() {
    return issuer;
  }

  public void setIssuer(String issuer) {
    this.issuer = issuer;
  }

  public AttributeMapPairings attributes(List<String> attributes) {
    this.attributes = attributes;
    return this;
  }

  public AttributeMapPairings addAttributesItem(String attributesItem) {
    if (this.attributes == null) {
      this.attributes = new ArrayList<String>();
    }
    this.attributes.add(attributesItem);
    return this;
  }

  /**
   * Array of attribute names (can be a single one) and/or string literals that will concatenate to form the group. Items starting with $ will be considered as attribute names, and will be substitutied by its value. Literal strings starting with $ must escape it \"\\$\"
   * @return attributes
  **/
  @ApiModelProperty(example = "[\"$surname1\",\"#\",\"$surname2\"]", value = "Array of attribute names (can be a single one) and/or string literals that will concatenate to form the group. Items starting with $ will be considered as attribute names, and will be substitutied by its value. Literal strings starting with $ must escape it \"\\$\"")


  public List<String> getAttributes() {
    return attributes;
  }

  public void setAttributes(List<String> attributes) {
    this.attributes = attributes;
  }

  public AttributeMapPairings regexp(String regexp) {
    this.regexp = regexp;
    return this;
  }

  /**
   * If set, the result of the concatenation will be matched towards this PERL compatible regexp (no match will return an empty string). Can be used to transform or to ensure a given format.
   * @return regexp
  **/
  @ApiModelProperty(example = "^(-,a-zA-Z)+#(-,a-zA-Z)+$", value = "If set, the result of the concatenation will be matched towards this PERL compatible regexp (no match will return an empty string). Can be used to transform or to ensure a given format.")


  public String getRegexp() {
    return regexp;
  }

  public void setRegexp(String regexp) {
    this.regexp = regexp;
  }

  public AttributeMapPairings replace(String replace) {
    this.replace = replace;
    return this;
  }

  /**
   * If set, this is what will be returned after matching the regexp. Sub-match numeric placeholders can be used as in a PERL compatible regexp.
   * @return replace
  **/
  @ApiModelProperty(example = "\\1 \\2", value = "If set, this is what will be returned after matching the regexp. Sub-match numeric placeholders can be used as in a PERL compatible regexp.")


  public String getReplace() {
    return replace;
  }

  public void setReplace(String replace) {
    this.replace = replace;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttributeMapPairings attributeMapPairings = (AttributeMapPairings) o;
    return Objects.equals(this.profile, attributeMapPairings.profile) &&
        Objects.equals(this.issuer, attributeMapPairings.issuer) &&
        Objects.equals(this.attributes, attributeMapPairings.attributes) &&
        Objects.equals(this.regexp, attributeMapPairings.regexp) &&
        Objects.equals(this.replace, attributeMapPairings.replace);
  }

  @Override
  public int hashCode() {
    return Objects.hash(profile, issuer, attributes, regexp, replace);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttributeMapPairings {\n");
    
    sb.append("    profile: ").append(toIndentedString(profile)).append("\n");
    sb.append("    issuer: ").append(toIndentedString(issuer)).append("\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
    sb.append("    regexp: ").append(toIndentedString(regexp)).append("\n");
    sb.append("    replace: ").append(toIndentedString(replace)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
