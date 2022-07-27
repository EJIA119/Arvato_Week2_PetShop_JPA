import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { IPet } from '../pet/pet';
import { IOwner } from './owner';

@Injectable({
  providedIn: 'root',
})
export class OwnerService {
  private ownerUrl = 'http://localhost:8080/owner';

  constructor(private http: HttpClient) {}

  findAll(): Observable<IOwner[]> {
    return this.http.get<IOwner[]>(this.ownerUrl + '/findAll').pipe(
      tap((data) => console.log('All', JSON.stringify(data))),
      catchError(this.handleError)
    );
  }

  findById(id: number): Observable<IOwner> {
    return this.http.get<IOwner>(this.ownerUrl + `/findById/${id}`).pipe(
      tap((data) => console.log('All', JSON.stringify(data))),
      catchError(this.handleError)
    );
  }

  addOwner(newOwner: IOwner): Observable<IOwner> {
    return this.http.post<IOwner>(this.ownerUrl + '/add', newOwner).pipe(
      tap((data) => console.log('All', JSON.stringify(data))),
      catchError(this.handleError)
    );
  }

  findPetByOwnerId(id: number): Observable<IPet[]> {
    return this.http
      .get<IPet[]>(this.ownerUrl + `/findPetByOwnerId/${id}`)
      .pipe(
        tap((data) => console.log('All', JSON.stringify(data))),
        catchError(this.handleError)
      );
  }

  findByPetId(id: number): Observable<IOwner> {
    return this.http.get<IOwner>(this.ownerUrl + `/findByPetId/${id}`).pipe(
      tap((data) => console.log('All', JSON.stringify(data))),
      catchError(this.handleError)
    );
  }

  findByPetName(name: string): Observable<IOwner[]> {
    return this.http
      .get<IOwner[]>(this.ownerUrl + `/findByPetName?name=${name}`)
      .pipe(
        tap((data) => console.log('All', JSON.stringify(data))),
        catchError(this.handleError)
      );
  }

  findByDateCreated(dateCreated: string): Observable<IOwner[]> {
    return this.http
      .get<IOwner[]>(
        this.ownerUrl + `/findByDateCreated?dateCreated=${dateCreated}`
      )
      .pipe(
        tap((data) => console.log('All', JSON.stringify(data))),
        catchError(this.handleError)
      );
  }

  createOrUpdateRelation(ownerID : number, pet: IPet) {
    return this.http.put<IPet>(this.ownerUrl + `/updateRelation/${ownerID}`, pet).pipe(
      tap((data) => console.log('All', JSON.stringify(data))),
      catchError(this.handleError)
    )
  }

  private handleError(err: HttpErrorResponse) {
    let errorMessage = '';
    if (err.error instanceof ErrorEvent) {
      errorMessage = `An error occurred: ${err.message}`;
    } else {
      errorMessage = `Server returned code: ${err.status}, error message is: ${err.message}`;
    }
    console.log(errorMessage);
    return throwError(() => err.error.message);
  }
}
