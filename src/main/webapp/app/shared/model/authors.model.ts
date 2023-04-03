import dayjs from 'dayjs';
import { IBook } from 'app/shared/model/book.model';

export interface IAuthors {
  id?: number;
  fullName?: string | null;
  birthDay?: string | null;
  dedDay?: string | null;
  books?: IBook[] | null;
}

export const defaultValue: Readonly<IAuthors> = {};
