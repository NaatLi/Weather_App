import 'package:bloc/bloc.dart';
import 'package:equatable/equatable.dart';
import 'package:geolocator/geolocator.dart';
import 'package:weather/weather.dart';

import '../data/my_data.dart';

part 'weather_bloc_event.dart';
part 'weather_bloc_state.dart';

class WeatherBlocBloc extends Bloc<WeatherBlocEvent, WeatherBlocState> {
  WeatherBlocBloc() : super(WeatherBlocInitial()) {
    on<FetchWeather>((event, emit) async {
			emit(WeatherBlocLoading());
      try {
				WeatherFactory wf = WeatherFactory(API_KEY, language: Language.RUSSIAN);
				
				Weather weather = await wf.currentWeatherByLocation(
					event.position.latitude, 
					event.position.longitude,
				);
				emit(WeatherBlocSuccess(weather));
      } catch (e) {
        emit(WeatherBlocFailure());
      }
    });
  }
}

sealed class WeatherBlocEvent extends Equatable {
  const WeatherBlocEvent();

    @override
    List<Object> get props => [];
}

class FetchWeather extends WeatherBlocEvent {
    final Position position;

  const FetchWeather(this.position);

    @override
    List<Object> get props => [position];
}

sealed class WeatherBlocState extends Equatable {
  const WeatherBlocState();

    @override
    List<Object> get props => [];
}

final class WeatherBlocInitial extends WeatherBlocState {}

final class WeatherBlocLoading extends WeatherBlocState {}
final class WeatherBlocFailure extends WeatherBlocState {}
final class WeatherBlocSuccess extends WeatherBlocState {
    final Weather weather;

  const WeatherBlocSuccess(this.weather);

    @override
    List<Object> get props => [weather];
}