import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICafe } from 'app/shared/model/cafe.model';
import { getEntity, updateEntity, createEntity, reset } from './cafe.reducer';

export const CafeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cafeEntity = useAppSelector(state => state.cafe.entity);
  const loading = useAppSelector(state => state.cafe.loading);
  const updating = useAppSelector(state => state.cafe.updating);
  const updateSuccess = useAppSelector(state => state.cafe.updateSuccess);

  const handleClose = () => {
    navigate('/cafe' + location.search);
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
    values.openingTime = convertDateTimeToServer(values.openingTime);
    values.closeTime = convertDateTimeToServer(values.closeTime);

    const entity = {
      ...cafeEntity,
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
      ? {
          openingTime: displayDefaultDateTime(),
          closeTime: displayDefaultDateTime(),
        }
      : {
          ...cafeEntity,
          openingTime: convertDateTimeFromServer(cafeEntity.openingTime),
          closeTime: convertDateTimeFromServer(cafeEntity.closeTime),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="studentsPlatformApp.cafe.home.createOrEditLabel" data-cy="CafeCreateUpdateHeading">
            <Translate contentKey="studentsPlatformApp.cafe.home.createOrEditLabel">Create or edit a Cafe</Translate>
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
                  id="cafe-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('studentsPlatformApp.cafe.description')}
                id="cafe-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField label={translate('studentsPlatformApp.cafe.name')} id="cafe-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                label={translate('studentsPlatformApp.cafe.location')}
                id="cafe-location"
                name="location"
                data-cy="location"
                type="text"
              />
              <ValidatedField
                label={translate('studentsPlatformApp.cafe.openingTime')}
                id="cafe-openingTime"
                name="openingTime"
                data-cy="openingTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('studentsPlatformApp.cafe.closeTime')}
                id="cafe-closeTime"
                name="closeTime"
                data-cy="closeTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/cafe" replace color="info">
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

export default CafeUpdate;
