import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IVacancy } from 'app/shared/model/vacancy.model';
import { getEntity, updateEntity, createEntity, reset } from './vacancy.reducer';

export const VacancyUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const vacancyEntity = useAppSelector(state => state.vacancy.entity);
  const loading = useAppSelector(state => state.vacancy.loading);
  const updating = useAppSelector(state => state.vacancy.updating);
  const updateSuccess = useAppSelector(state => state.vacancy.updateSuccess);

  const handleClose = () => {
    navigate('/vacancy' + location.search);
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
      ...vacancyEntity,
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
          ...vacancyEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="studentsPlatformApp.vacancy.home.createOrEditLabel" data-cy="VacancyCreateUpdateHeading">
            <Translate contentKey="studentsPlatformApp.vacancy.home.createOrEditLabel">Create or edit a Vacancy</Translate>
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
                  id="vacancy-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('studentsPlatformApp.vacancy.companyName')}
                id="vacancy-companyName"
                name="companyName"
                data-cy="companyName"
                type="text"
              />
              <ValidatedField
                label={translate('studentsPlatformApp.vacancy.jobType')}
                id="vacancy-jobType"
                name="jobType"
                data-cy="jobType"
                type="text"
              />
              <ValidatedField
                label={translate('studentsPlatformApp.vacancy.description')}
                id="vacancy-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('studentsPlatformApp.vacancy.vacancyCount')}
                id="vacancy-vacancyCount"
                name="vacancyCount"
                data-cy="vacancyCount"
                type="text"
              />
              <ValidatedField
                label={translate('studentsPlatformApp.vacancy.location')}
                id="vacancy-location"
                name="location"
                data-cy="location"
                type="text"
              />
              <ValidatedField
                label={translate('studentsPlatformApp.vacancy.phone')}
                id="vacancy-phone"
                name="phone"
                data-cy="phone"
                type="text"
              />
              <ValidatedField
                label={translate('studentsPlatformApp.vacancy.salary')}
                id="vacancy-salary"
                name="salary"
                data-cy="salary"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/vacancy" replace color="info">
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

export default VacancyUpdate;
