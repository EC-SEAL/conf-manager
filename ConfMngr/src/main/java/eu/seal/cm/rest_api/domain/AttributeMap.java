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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * Represent semantic equivalence relationships between attribute groups. Groups can be either single attributes or formed by regexp transformation (match and replace) of the result of concatenation of attributes and literal strings.
 */
@ApiModel(description = "Represent semantic equivalence relationships between attribute groups. Groups can be either single attributes or formed by regexp transformation (match and replace) of the result of concatenation of attributes and literal strings.")
@Validated

public class AttributeMap   {
  @JsonProperty("description")
  private String description = null;

  @JsonProperty("pairings")
  @Valid
  private List<AttributeMapPairings> pairings = null;

  public AttributeMap description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Name or explain the mapping
   * @return description
  **/
  @ApiModelProperty(value = "Name or explain the mapping")


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public AttributeMap pairings(List<AttributeMapPairings> pairings) {
    this.pairings = pairings;
    return this;
  }

  public AttributeMap addPairingsItem(AttributeMapPairings pairingsItem) {
    if (this.pairings == null) {
      this.pairings = new ArrayList<AttributeMapPairings>();
    }
    this.pairings.add(pairingsItem);
    return this;
  }

  /**
   * A list of groups that are semantically equivalent
   * @return pairings
  **/
  @ApiModelProperty(value = "A list of groups that are semantically equivalent")

  @Valid

  public List<AttributeMapPairings> getPairings() {
    return pairings;
  }

  public void setPairings(List<AttributeMapPairings> pairings) {
    this.pairings = pairings;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttributeMap attributeMap = (AttributeMap) o;
    return Objects.equals(this.description, attributeMap.description) &&
        Objects.equals(this.pairings, attributeMap.pairings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, pairings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttributeMap {\n");
    
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    pairings: ").append(toIndentedString(pairings)).append("\n");
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

