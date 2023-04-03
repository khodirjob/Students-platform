import book from 'app/entities/book/book.reducer';
import authors from 'app/entities/authors/authors.reducer';
import category from 'app/entities/category/category.reducer';
import vacancy from 'app/entities/vacancy/vacancy.reducer';
import cafe from 'app/entities/cafe/cafe.reducer';
import attachment from 'app/entities/attachment/attachment.reducer';
import food from 'app/entities/food/food.reducer';
import room from 'app/entities/room/room.reducer';
import apartment from 'app/entities/apartment/apartment.reducer';
import schedule from 'app/entities/schedule/schedule.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  book,
  authors,
  category,
  vacancy,
  cafe,
  attachment,
  food,
  room,
  apartment,
  schedule,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
