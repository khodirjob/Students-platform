import { ICategory } from 'app/shared/model/category.model';
import { IAuthors } from 'app/shared/model/authors.model';

export interface IBook {
  id?: number;
  name?: string | null;
  description?: string | null;
  category?: ICategory | null;
  authors?: IAuthors[] | null;
}

export const defaultValue: Readonly<IBook> = {};
