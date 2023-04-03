import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICategory } from 'app/shared/model/category.model';
import { getEntities as getCategories } from 'app/entities/category/category.reducer';
import { IAuthors } from 'app/shared/model/authors.model';
import { getEntities as getAuthors } from 'app/entities/authors/authors.reducer';
import { IBook } from 'app/shared/model/book.model';
import { getEntity, updateEntity, createEntity, reset } from './book.reducer';

export const BookUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const categories = useAppSelector(state => state.category.entities);
  const authors = useAppSelector(state => state.authors.entities);
  const bookEntity = useAppSelector(state => state.book.entity);
  const loading = useAppSelector(state => state.book.loading);
  const updating = useAppSelector(state => state.book.updating);
  const updateSuccess = useAppSelector(state => state.book.updateSuccess);

  const handleClose = () => {
    navigate('/book' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCategories({}));
    dispatch(getAuthors({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...bookEntity,
      ...values,
      authors: mapIdList(values.authors),
      category: categories.find(it => it.id.toString() === values.category.toString()),
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
          ...bookEntity,
          category: bookEntity?.category?.id,
          authors: bookEntity?.authors?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="studentsPlatformApp.book.home.createOrEditLabel" data-cy="BookCreateUpdateHeading">
            <Translate contentKey="studentsPlatformApp.book.home.createOrEditLabel">Create or edit a Book</Translate>
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
                  id="book-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('studentsPlatformApp.book.name')} id="book-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                label={translate('studentsPlatformApp.book.description')}
                id="book-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                id="book-category"
                name="category"
                data-cy="category"
                label={translate('studentsPlatformApp.book.category')}
                type="select"
              >
                <option value="" key="0" />
                {categories
                  ? categories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('studentsPlatformApp.book.authors')}
                id="book-authors"
                data-cy="authors"
                type="select"
                multiple
                name="authors"
              >
                <option value="" key="0" />
                {authors
                  ? authors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.fullName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/book" replace color="info">
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

export default BookUpdate;
