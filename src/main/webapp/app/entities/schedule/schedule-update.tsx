import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISchedule } from 'app/shared/model/schedule.model';
import { getEntity, updateEntity, createEntity, reset } from './schedule.reducer';

export const ScheduleUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const scheduleEntity = useAppSelector(state => state.schedule.entity);
  const loading = useAppSelector(state => state.schedule.loading);
  const updating = useAppSelector(state => state.schedule.updating);
  const updateSuccess = useAppSelector(state => state.schedule.updateSuccess);

  const handleClose = () => {
    navigate('/schedule' + location.search);
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
      ...scheduleEntity,
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
          ...scheduleEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="studentsPlatformApp.schedule.home.createOrEditLabel" data-cy="ScheduleCreateUpdateHeading">
            <Translate contentKey="studentsPlatformApp.schedule.home.createOrEditLabel">Create or edit a Schedule</Translate>
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
                  id="schedule-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('studentsPlatformApp.schedule.faculty')}
                id="schedule-faculty"
                name="faculty"
                data-cy="faculty"
                type="text"
              />
              <ValidatedField
                label={translate('studentsPlatformApp.schedule.level')}
                id="schedule-level"
                name="level"
                data-cy="level"
                type="text"
              />
              <ValidatedField
                label={translate('studentsPlatformApp.schedule.description')}
                id="schedule-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/schedule" replace color="info">
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

export default ScheduleUpdate;
