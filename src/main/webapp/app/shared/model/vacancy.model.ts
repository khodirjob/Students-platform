export interface IVacancy {
  id?: number;
  companyName?: string | null;
  jobType?: string | null;
  description?: string | null;
  vacancyCount?: number | null;
  location?: string | null;
  phone?: string | null;
  salary?: number | null;
}

export const defaultValue: Readonly<IVacancy> = {};
