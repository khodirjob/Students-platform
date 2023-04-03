import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IBook } from 'app/shared/model/book.model';
import { getEntities as getBooks } from 'app/entities/book/book.reducer';
import { IAuthors } from 'app/shared/model/authors.model';
import { getEntity, updateEntity, createEntity, reset } from './authors.reducer';

export const AuthorsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const books = useAppSelector(state => state.book.entities);
  const authorsEntity = useAppSelector(state => state.authors.entity);
  const loading = useAppSelector(state => state.authors.loading);
  const updating = useAppSelector(state => state.authors.updating);
  const updateSuccess = useAppSelector(state => state.authors.updateSuccess);

  const handleClose = () => {
    navigate('/authors' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getBooks({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.birthDay = convertDateTimeToServer(values.birthDay);
    values.dedDay = convertDateTimeToServer(values.dedDay);

    const entity = {
      ...authorsEntity,
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
          birthDay: displayDefaultDateTime(),
          dedDay: displayDefaultDateTime(),
        }
      : {
          ...authorsEntity,
          birthDay: convertDateTimeFromServer(authorsEntity.birthDay),
          dedDay: convertDateTimeFromServer(authorsEntity.dedDay),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="studentsPlatformApp.authors.home.createOrEditLabel" data-cy="AuthorsCreateUpdateHeading">
            <Translate contentKey="studentsPlatformApp.authors.home.createOrEditLabel">Create or edit a Authors</Translate>
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
                  id="authors-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('studentsPlatformApp.authors.fullName')}
                id="authors-fullName"
                name="fullName"
                data-cy="fullName"
                type="text"
              />
              <ValidatedField
                label={translate('studentsPlatformApp.authors.birthDay')}
                id="authors-birthDay"
                name="birthDay"
                data-cy="birthDay"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('studentsPlatformApp.authors.dedDay')}
                id="authors-dedDay"
                name="dedDay"
                data-cy="dedDay"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/authors" replace color="info">
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

export default AuthorsUpdate;
