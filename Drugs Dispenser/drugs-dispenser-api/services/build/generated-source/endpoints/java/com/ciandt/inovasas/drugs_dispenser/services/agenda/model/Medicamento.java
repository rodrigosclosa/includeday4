/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://github.com/google/apis-client-generator/
 * (build: 2016-07-08 17:28:43 UTC)
 * on 2016-09-16 at 19:50:30 UTC 
 * Modify at your own risk.
 */

package com.ciandt.inovasas.drugs_dispenser.services.agenda.model;

/**
 * Model definition for Medicamento.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the agenda. For a detailed explanation see:
 * <a href="https://developers.google.com/api-client-library/java/google-http-java-client/json">https://developers.google.com/api-client-library/java/google-http-java-client/json</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class Medicamento extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer codigoMedicamento;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String descricaoMedicamento;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long id;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private com.google.api.client.util.DateTime validadeMedicamento;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getCodigoMedicamento() {
    return codigoMedicamento;
  }

  /**
   * @param codigoMedicamento codigoMedicamento or {@code null} for none
   */
  public Medicamento setCodigoMedicamento(java.lang.Integer codigoMedicamento) {
    this.codigoMedicamento = codigoMedicamento;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getDescricaoMedicamento() {
    return descricaoMedicamento;
  }

  /**
   * @param descricaoMedicamento descricaoMedicamento or {@code null} for none
   */
  public Medicamento setDescricaoMedicamento(java.lang.String descricaoMedicamento) {
    this.descricaoMedicamento = descricaoMedicamento;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getId() {
    return id;
  }

  /**
   * @param id id or {@code null} for none
   */
  public Medicamento setId(java.lang.Long id) {
    this.id = id;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public com.google.api.client.util.DateTime getValidadeMedicamento() {
    return validadeMedicamento;
  }

  /**
   * @param validadeMedicamento validadeMedicamento or {@code null} for none
   */
  public Medicamento setValidadeMedicamento(com.google.api.client.util.DateTime validadeMedicamento) {
    this.validadeMedicamento = validadeMedicamento;
    return this;
  }

  @Override
  public Medicamento set(String fieldName, Object value) {
    return (Medicamento) super.set(fieldName, value);
  }

  @Override
  public Medicamento clone() {
    return (Medicamento) super.clone();
  }

}
