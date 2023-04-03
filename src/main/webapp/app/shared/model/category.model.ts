export interface ICategory {
  id?: number;
  name?: string | null;
  description?: string | null;
}

export const defaultValue: Readonly<ICategory> = {};
