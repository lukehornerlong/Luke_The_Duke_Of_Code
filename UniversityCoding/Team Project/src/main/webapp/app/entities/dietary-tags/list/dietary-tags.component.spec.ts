import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DietaryTagsService } from '../service/dietary-tags.service';

import { DietaryTagsComponent } from './dietary-tags.component';

describe('DietaryTags Management Component', () => {
  let comp: DietaryTagsComponent;
  let fixture: ComponentFixture<DietaryTagsComponent>;
  let service: DietaryTagsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'dietary-tags', component: DietaryTagsComponent }]), HttpClientTestingModule],
      declarations: [DietaryTagsComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(DietaryTagsComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DietaryTagsComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DietaryTagsService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.dietaryTags?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to dietaryTagsService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getDietaryTagsIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getDietaryTagsIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
