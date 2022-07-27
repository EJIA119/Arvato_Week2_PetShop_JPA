import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, Observable, tap, throwError } from "rxjs";
import { IPet, ITopName } from "./pet";

@Injectable({
    providedIn:'root'
})
export class PetService {

    private petUrl = 'http://localhost:8080/pet';

    constructor(private http: HttpClient){}

    create(newPet : IPet): Observable<IPet> {
      return this.http.post<IPet>(this.petUrl + "/add", newPet).pipe(
        tap((data) => console.log('All', JSON.stringify(data))),
        catchError(this.handleError)
      );
    }

    findAll(): Observable<IPet[]> {
        return this.http.get<IPet[]>(this.petUrl + "/findAll").pipe(
            tap((data) => console.log('All', JSON.stringify(data))),
            catchError(this.handleError)
          );
    }

    findById(id : number): Observable<IPet> {
      return this.http.get<IPet>(this.petUrl + `/findById/${id}`).pipe(
        tap((data) => console.log('All', JSON.stringify(data))),
        catchError(this.handleError)
      );
    }

    update(pet : IPet): Observable<IPet> {

      console.log('Update Pet: ', JSON.stringify(pet));

      return this.http.put<IPet>(this.petUrl + "/update/", pet).pipe(
        tap((data) => console.log('All', JSON.stringify(data))),
        catchError(this.handleError)
      );
    }

    delete(id : number) {
      return this.http.delete<number>(this.petUrl + `/delete/${id}`).pipe(
        tap((data) => console.log('All', JSON.stringify(data))),
        catchError(this.handleError)
      );
    }

    topName(): Observable<ITopName[]> {
      return this.http.get<ITopName[]>(this.petUrl + "/topName").pipe(
        tap((data) => console.log('All', JSON.stringify(data))),
        catchError(this.handleError)
      );
    }

    orphanPet(): Observable<IPet[]> {
      return this.http.get<IPet[]>(this.petUrl + "/orphanPet").pipe(
        tap((data) => console.log('All', JSON.stringify(data))),
        catchError(this.handleError)
      );
    }

    private handleError(err: HttpErrorResponse) {
        let errorMessage = '';
        if (err.error instanceof ErrorEvent) {
          errorMessage = `An error occurred: ${err.error.message}`;
        } else {
          errorMessage = `Server returned code: ${err.status}, error message is: ${err.message}`;
        }
        console.error(errorMessage);
        return throwError(() => err.error.message);
      }
}