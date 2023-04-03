import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IApartment } from 'app/shared/model/apartment.model';
import { getEntity, updateEntity, createEntity, reset } from './apartment.reducer';

export const ApartmentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const apartmentEntity = useAppSelector(state => state.apartment.entity);
  const loading = useAppSelector(state => state.apartment.loading);
  const updating = useAppSelector(state => state.apartment.updating);
  const updateSuccess = useAppSelector(state => state.apartment.updateSuccess);

  const handleClose = () => {
    navigate('/apartment' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...apartmentEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...apartmentEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="studentsPlatformApp.apartment.home.createOrEditLabel" data-cy="ApartmentCreateUpdateHeading">
            <Translate contentKey="studentsPlatformApp.apartment.home.createOrEditLabel">Create or edit a Apartment</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="apartment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('studentsPlatformApp.apartment.location')}
                id="apartment-location"
                name="location"
                data-cy="location"
                type="text"
              />
              <ValidatedField
                label={translate('studentsPlatformApp.apartment.phone')}
                id="apartment-phone"
                name="phone"
                data-cy="phone"
                type="text"
              />
              <ValidatedField
                label={translate('studentsPlatformApp.apartment.description')}
                id="apartment-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('studentsPlatformApp.apartment.price')}
                id="apartment-price"
                name="price"
                data-cy="price"
                type="text"
              />
              <ValidatedField
                label={translate('studentsPlatformApp.apartment.requirements')}
                id="apartment-requirements"
                name="requirements"
                data-cy="requirements"
                type="text"
              />
              <ValidatedField
                label={translate('studentsPlatformApp.apartment.roomFit')}
                id="apartment-roomFit"
                name="roomFit"
                data-cy="roomFit"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/apartment" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ApartmentUpdate;
